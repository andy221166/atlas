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
import org.atlas.platform.event.contract.order.model.OrderItem;
import org.atlas.platform.event.contract.order.model.User;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.platform.sequencegenerator.SequenceGenerator;
import org.atlas.platform.sequencegenerator.SequenceType;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.atlas.service.order.domain.entity.OrderItemEntity;
import org.atlas.service.order.domain.shared.OrderStatus;
import org.atlas.service.order.port.inbound.usecase.front.PlaceOrderUseCase;
import org.atlas.service.order.port.outbound.event.OrderEventPublisherPort;
import org.atlas.service.order.port.outbound.repository.OrderRepositoryPort;
import org.atlas.service.product.port.inbound.usecase.internal.ListProductUseCase;
import org.atlas.service.product.port.inbound.usecase.internal.ListProductUseCase.Output.Product;
import org.atlas.service.user.port.inbound.usecase.internal.ListUserUseCase;
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
  public Output handle(Input input) {
    OrderEntity orderEntity = newOrder(input);
    orderEntity.setCode(sequenceGenerator.generate(SequenceType.ORDER));

    // Fetch user and products info from internal services
    ListUserUseCase.Output.User user = fetchUser(orderEntity);
    Map<Integer, ListProductUseCase.Output.Product> products = fetchProducts(orderEntity);

    // Update price of item
    orderEntity.getOrderItems()
        .forEach(orderItemEntity -> {
          ListProductUseCase.Output.Product product = products.get(orderItemEntity.getProductId());
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

    return new Output(orderEntity.getId());
  }

  private OrderEntity newOrder(Input input) {
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setUserId(UserContext.getUserId());
    orderEntity.setStatus(OrderStatus.PROCESSING);
    orderEntity.setCreatedAt(new Date());
    for (Input.OrderItem orderItemInput : input.getOrderItems()) {
      OrderItemEntity orderItemEntity = new OrderItemEntity();
      orderItemEntity.setProductId(orderItemInput.getProductId());
      orderItemEntity.setQuantity(orderItemInput.getQuantity());
      orderEntity.addOrderItem(orderItemEntity);
    }
    return orderEntity;
  }

  private ListUserUseCase.Output.User fetchUser(OrderEntity orderEntity) {
    Integer userId = orderEntity.getUserId();
    ListUserUseCase.Input input = new ListUserUseCase.Input(Collections.singletonList(userId));
    ListUserUseCase.Output output = userApiClient.call(input);
    return output.getUsers().get(0);
  }

  private Map<Integer, ListProductUseCase.Output.Product> fetchProducts(OrderEntity orderEntity) {
    List<Integer> productIds = orderEntity.getOrderItems()
        .stream()
        .map(OrderItemEntity::getProductId)
        .toList();
    ListProductUseCase.Input input = new ListProductUseCase.Input(productIds);
    ListProductUseCase.Output output = productApiClient.call(input);
    return output.getProducts()
        .stream()
        .collect(Collectors.toMap(ListProductUseCase.Output.Product::getId, Function.identity()));
  }

  private void publishEvent(OrderEntity orderEntity,
      ListUserUseCase.Output.User user,
      Map<Integer, ListProductUseCase.Output.Product> products) {
    OrderCreatedEvent event = new OrderCreatedEvent(applicationConfigService.getApplicationName());
    ObjectMapperUtil.getInstance().merge(orderEntity, event);
    event.setOrderId(orderEntity.getId());
    event.setUser(ObjectMapperUtil.getInstance().map(user, User.class));
    event.getOrderItems().forEach(orderItem -> {
      ListProductUseCase.Output.Product product = products.get(orderItem.getProduct().getId());
      ObjectMapperUtil.getInstance().merge(product, orderItem.getProduct());
    });
    orderEventPublisherPort.publish(event);
  }
}
