package org.atlas.domain.order.usecase.front;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.domain.order.entity.OrderItemEntity;
import org.atlas.domain.order.repository.OrderRepository;
import org.atlas.domain.order.shared.OrderStatus;
import org.atlas.domain.order.usecase.front.FrontPlaceOrderUseCaseHandler.PlaceOrderInput;
import org.atlas.domain.order.usecase.front.FrontPlaceOrderUseCaseHandler.PlaceOrderOutput;
import org.atlas.domain.product.shared.internal.ListProductInput;
import org.atlas.domain.product.shared.internal.ListProductOutput;
import org.atlas.domain.user.shared.internal.ListUserInput;
import org.atlas.domain.user.shared.internal.ListUserOutput;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.error.AppError;
import org.atlas.framework.event.contract.order.OrderCreatedEvent;
import org.atlas.framework.event.contract.order.model.User;
import org.atlas.framework.event.publisher.OrderEventPublisherPort;
import org.atlas.framework.exception.BusinessException;
import org.atlas.framework.internalapi.ProductApiPort;
import org.atlas.framework.internalapi.UserApiPort;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.security.UserContext;
import org.atlas.framework.sequencegenerator.SequenceGenerator;
import org.atlas.framework.sequencegenerator.SequenceType;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
@Slf4j
public class FrontPlaceOrderUseCaseHandler implements
    UseCaseHandler<PlaceOrderInput, PlaceOrderOutput> {

  private final OrderRepository orderRepository;
  private final ApplicationConfigPort applicationConfigPort;
  private final OrderEventPublisherPort orderEventPublisherPort;
  private final ProductApiPort productApiPort;
  private final SequenceGenerator sequenceGenerator;
  private final UserApiPort userApiPort;

  @Override
  public PlaceOrderOutput handle(PlaceOrderInput input) {
    try {
      OrderEntity orderEntity = newOrder(input);
      orderEntity.setCode(sequenceGenerator.generate(SequenceType.ORDER));

      // Fetch user and products info from internal services
      ListUserOutput.User user = fetchUser(orderEntity);
      Map<Integer, ListProductOutput.Product> products = fetchProducts(orderEntity);

      // Update price of item
      orderEntity.getOrderItems()
          .forEach(orderItemEntity -> {
            ListProductOutput.Product product = products.get(orderItemEntity.getProductId());
            if (product == null) {
              log.error("SearchResponse not found: productId={}", orderItemEntity.getProductId());
              throw new BusinessException(AppError.PRODUCT_NOT_FOUND);
            }
            orderItemEntity.setProductPrice(product.getPrice());
          });

      // Calculate order amount
      orderEntity.calculateOrderAmount();

      // Save into DB
      orderRepository.insert(orderEntity);

      // Publish event
      publishEvent(orderEntity, user, products);

      return new PlaceOrderOutput(orderEntity.getId());
    } catch (Exception e) {
      log.error("Failed to place order", e);
      throw new BusinessException(AppError.FAILED_TO_PLACE_ORDER);
    }
  }

  private OrderEntity newOrder(PlaceOrderInput input) {
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setUserId(UserContext.getUserId());
    orderEntity.setStatus(OrderStatus.PROCESSING);
    orderEntity.setCreatedAt(new Date());
    for (PlaceOrderInput.OrderItem orderItemInput : input.getOrderItems()) {
      OrderItemEntity orderItemEntity = new OrderItemEntity();
      orderItemEntity.setProductId(orderItemInput.getProductId());
      orderItemEntity.setQuantity(orderItemInput.getQuantity());
      orderEntity.addOrderItem(orderItemEntity);
    }
    return orderEntity;
  }

  private ListUserOutput.User fetchUser(OrderEntity orderEntity) {
    Integer userId = orderEntity.getUserId();
    ListUserInput input = new ListUserInput(Collections.singletonList(userId));
    ListUserOutput output = userApiPort.call(input);
    return output.getUsers().get(0);
  }

  private Map<Integer, ListProductOutput.Product> fetchProducts(OrderEntity orderEntity) {
    List<Integer> productIds = orderEntity.getOrderItems()
        .stream()
        .map(OrderItemEntity::getProductId)
        .toList();
    ListProductInput input = new ListProductInput(productIds);
    ListProductOutput output = productApiPort.call(input);
    return output.getProducts()
        .stream()
        .collect(Collectors.toMap(ListProductOutput.Product::getId, Function.identity()));
  }

  private void publishEvent(OrderEntity orderEntity,
      ListUserOutput.User user,
      Map<Integer, ListProductOutput.Product> products) {
    OrderCreatedEvent event = new OrderCreatedEvent(applicationConfigPort.getApplicationName());
    ObjectMapperUtil.getInstance()
        .merge(orderEntity, event);
    event.setOrderId(orderEntity.getId());
    event.setUser(ObjectMapperUtil.getInstance().map(user, User.class));
    event.getOrderItems().forEach(orderItem -> {
      ListProductOutput.Product product = products.get(orderItem.getProduct().getId());
      ObjectMapperUtil.getInstance().merge(product, orderItem.getProduct());
    });
    orderEventPublisherPort.publish(event);
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PlaceOrderInput {

    @NotEmpty
    private List<@Valid OrderItem> orderItems;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {

      @NotNull
      private Integer productId;

      @NotNull
      @Min(1)
      private Integer quantity;
    }
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PlaceOrderOutput {

    private Integer orderId;
  }
}
