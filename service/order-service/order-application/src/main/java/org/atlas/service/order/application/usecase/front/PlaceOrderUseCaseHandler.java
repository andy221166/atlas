package org.atlas.service.order.application.usecase.front;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.api.client.product.ProductApiClient;
import org.atlas.platform.api.client.user.UserApiClient;
import org.atlas.platform.commons.context.UserContext;
import org.atlas.platform.commons.enums.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.config.ApplicationConfigService;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.platform.event.contract.order.model.User;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.platform.sequencegenerator.SequenceGenerator;
import org.atlas.platform.sequencegenerator.SequenceType;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.atlas.service.order.domain.entity.OrderItemEntity;
import org.atlas.service.order.domain.shared.OrderStatus;
import org.atlas.service.order.port.inbound.front.PlaceOrderUseCase;
import org.atlas.service.order.port.outbound.event.OrderEventPublisherPort;
import org.atlas.service.order.port.outbound.repository.OrderRepositoryPort;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase.ListProductInput;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase.ListProductOutput;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase.ListUserInput;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase.ListUserOutput;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlaceOrderUseCaseHandler implements PlaceOrderUseCase {

  private final SequenceGenerator sequenceGenerator;
  private final OrderRepositoryPort orderRepositoryPort;
  private final OrderEventPublisherPort orderEventPublisherPort;
  private final UserApiClient userApiClient;
  private final ProductApiClient productApiClient;
  private final ApplicationConfigService applicationConfigService;

  @Override
  @Transactional
  public PlaceOrderOutput handle(PlaceOrderInput input) {
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
            log.error("Product not found: productId={}", orderItemEntity.getProductId());
            throw new BusinessException(AppError.PRODUCT_NOT_FOUND);
          }
          orderItemEntity.setProductPrice(product.getPrice());
        });

    // Calculate order amount
    orderEntity.calculateOrderAmount();

    // Save into DB
    orderRepositoryPort.insert(orderEntity);

    // Publish event
    publishEvent(orderEntity, user, products);

    return new PlaceOrderOutput(orderEntity.getId());
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
    ListUserOutput output = userApiClient.call(input);
    return output.getUsers().get(0);
  }

  private Map<Integer, ListProductOutput.Product> fetchProducts(OrderEntity orderEntity) {
    List<Integer> productIds = orderEntity.getOrderItems()
        .stream()
        .map(OrderItemEntity::getProductId)
        .toList();
    ListProductInput input = new ListProductInput(productIds);
    ListProductOutput output = productApiClient.call(input);
    return output.getProducts()
        .stream()
        .collect(Collectors.toMap(ListProductOutput.Product::getId, Function.identity()));
  }

  private void publishEvent(OrderEntity orderEntity,
      ListUserOutput.User user,
      Map<Integer, ListProductOutput.Product> products) {
    OrderCreatedEvent event = new OrderCreatedEvent(applicationConfigService.getApplicationName());
    ObjectMapperUtil.getInstance().merge(orderEntity, event);
    event.setOrderId(orderEntity.getId());
    event.setUser(ObjectMapperUtil.getInstance().map(user, User.class));
    event.getOrderItems().forEach(orderItem -> {
      ListProductOutput.Product product = products.get(orderItem.getProduct().getId());
      ObjectMapperUtil.getInstance().merge(product, orderItem.getProduct());
    });
    orderEventPublisherPort.publish(event);
  }
}
