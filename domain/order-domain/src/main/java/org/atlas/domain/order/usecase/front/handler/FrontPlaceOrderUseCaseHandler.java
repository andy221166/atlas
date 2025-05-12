package org.atlas.domain.order.usecase.front.handler;

import java.util.Collections;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.domain.order.entity.OrderItemEntity;
import org.atlas.domain.order.entity.ProductEntity;
import org.atlas.domain.order.entity.UserEntity;
import org.atlas.domain.order.port.messaging.OrderMessagePublisherPort;
import org.atlas.domain.order.repository.OrderRepository;
import org.atlas.domain.order.service.OrderAggregator;
import org.atlas.domain.order.shared.enums.OrderStatus;
import org.atlas.domain.order.usecase.front.model.FrontPlaceOrderInput;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.context.Contexts;
import org.atlas.framework.domain.event.contract.order.OrderCreatedEvent;
import org.atlas.framework.domain.exception.DomainException;
import org.atlas.framework.error.AppError;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.sequencegenerator.SequenceGenerator;
import org.atlas.framework.sequencegenerator.enums.SequenceType;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
@Slf4j
public class FrontPlaceOrderUseCaseHandler implements
    UseCaseHandler<FrontPlaceOrderInput, Integer> {

  private final OrderRepository orderRepository;
  private final OrderAggregator orderAggregator;
  private final ApplicationConfigPort applicationConfigPort;
  private final OrderMessagePublisherPort orderMessagePublisherPort;
  private final SequenceGenerator sequenceGenerator;

  @Override
  public Integer handle(FrontPlaceOrderInput input) {
    try {
      OrderEntity orderEntity = newOrder(input);
      orderEntity.setCode(sequenceGenerator.generate(SequenceType.ORDER));

      // Fetch user and products info from internal services
      orderAggregator.aggregate(Collections.singletonList(orderEntity), false);

      // Calculate order amount
      orderEntity.calculateOrderAmount();

      // Save into DB
      orderRepository.insert(orderEntity);

      // Publish event
      publishEvent(orderEntity);

      // Return the inserted order ID
      return orderEntity.getId();
    } catch (Exception e) {
      log.error("Failed to place order", e);
      throw new DomainException(AppError.FAILED_TO_PLACE_ORDER);
    }
  }

  private OrderEntity newOrder(FrontPlaceOrderInput input) {
    // Order
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setStatus(OrderStatus.PROCESSING);
    orderEntity.setCreatedAt(new Date());

    // User
    UserEntity userEntity = UserEntity.builder()
        .id(Contexts.getUserId())
        .build();
    orderEntity.setUser(userEntity);

    // Order Items
    for (FrontPlaceOrderInput.OrderItem orderItemInput : input.getOrderItems()) {
      OrderItemEntity orderItemEntity = new OrderItemEntity();
      orderItemEntity.setQuantity(orderItemInput.getQuantity());

      // Product
      ProductEntity productEntity = ProductEntity.builder()
          .id(orderItemInput.getProductId())
          .build();
      orderItemEntity.setProduct(productEntity);

      orderEntity.addOrderItem(orderItemEntity);
    }
    return orderEntity;
  }

  private void publishEvent(OrderEntity orderEntity) {
    OrderCreatedEvent event = new OrderCreatedEvent(applicationConfigPort.getApplicationName());
    ObjectMapperUtil.getInstance().merge(orderEntity, event);
    orderMessagePublisherPort.publish(event);
  }
}
