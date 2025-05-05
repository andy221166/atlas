package org.atlas.domain.order.event;

import lombok.RequiredArgsConstructor;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.domain.order.port.messaging.OrderMessagePublisherPort;
import org.atlas.domain.order.repository.OrderRepository;
import org.atlas.domain.order.shared.OrderStatus;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.error.AppError;
import org.atlas.framework.domain.event.contract.order.OrderCanceledEvent;
import org.atlas.framework.domain.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.framework.domain.event.handler.EventHandler;
import org.atlas.framework.domain.exception.DomainException;

@RequiredArgsConstructor
public class ReserveQuantityFailedEventHandler implements EventHandler<ReserveQuantityFailedEvent> {

  private final OrderRepository orderRepository;
  private final ApplicationConfigPort applicationConfigPort;
  private final OrderMessagePublisherPort orderMessagePublisherPort;

  @Override
  public void handle(ReserveQuantityFailedEvent reserveQuantityFailedEvent) {
    // Find order
    OrderEntity orderEntity = orderRepository.findById(reserveQuantityFailedEvent.getOrderId())
        .orElseThrow(() -> new DomainException(AppError.ORDER_NOT_FOUND));
    if (orderEntity.getStatus() != OrderStatus.PROCESSING) {
      throw new DomainException(AppError.ORDER_INVALID_STATUS);
    }

    // Update order
    orderEntity.setStatus(OrderStatus.CANCELED);
    orderEntity.setCanceledReason(reserveQuantityFailedEvent.getError());
    orderRepository.update(orderEntity);

    // Publish event
    OrderCanceledEvent orderCanceledEvent = new OrderCanceledEvent(
        applicationConfigPort.getApplicationName());
    orderCanceledEvent.merge(reserveQuantityFailedEvent);
    orderCanceledEvent.setCanceledReason(orderEntity.getCanceledReason());
    orderMessagePublisherPort.publish(orderCanceledEvent);
  }
}
