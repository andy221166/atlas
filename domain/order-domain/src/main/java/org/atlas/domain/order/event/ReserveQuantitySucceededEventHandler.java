package org.atlas.domain.order.event;

import lombok.RequiredArgsConstructor;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.domain.order.port.messaging.OrderMessagePublisherPort;
import org.atlas.domain.order.repository.OrderRepository;
import org.atlas.domain.order.shared.enums.OrderStatus;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.error.AppError;
import org.atlas.framework.domain.event.contract.order.OrderConfirmedEvent;
import org.atlas.framework.domain.event.contract.product.ReserveQuantitySucceededEvent;
import org.atlas.framework.domain.event.handler.EventHandler;
import org.atlas.framework.domain.exception.DomainException;

@RequiredArgsConstructor
public class ReserveQuantitySucceededEventHandler implements
    EventHandler<ReserveQuantitySucceededEvent> {

  private final OrderRepository orderRepository;
  private final ApplicationConfigPort applicationConfigPort;
  private final OrderMessagePublisherPort orderMessagePublisherPort;

  @Override
  public void handle(ReserveQuantitySucceededEvent reserveQuantitySucceededEvent) {
    // Find order
    OrderEntity orderEntity = orderRepository.findById(reserveQuantitySucceededEvent.getOrderId())
        .orElseThrow(() -> new DomainException(AppError.ORDER_NOT_FOUND));
    if (orderEntity.getStatus() != OrderStatus.PROCESSING) {
      throw new DomainException(AppError.ORDER_INVALID_STATUS);
    }

    // Update order
    orderEntity.setStatus(OrderStatus.CONFIRMED);
    orderRepository.update(orderEntity);

    // Publish event
    OrderConfirmedEvent orderConfirmedEvent = new OrderConfirmedEvent(
        applicationConfigPort.getApplicationName());
    orderConfirmedEvent.merge(reserveQuantitySucceededEvent);
    orderMessagePublisherPort.publish(orderConfirmedEvent);
  }
}
