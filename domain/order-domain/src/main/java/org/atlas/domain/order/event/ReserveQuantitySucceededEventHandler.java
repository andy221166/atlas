package org.atlas.domain.order.event;

import lombok.RequiredArgsConstructor;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.domain.order.repository.OrderRepository;
import org.atlas.domain.order.shared.OrderStatus;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.error.AppError;
import org.atlas.framework.event.contract.order.OrderConfirmedEvent;
import org.atlas.framework.event.contract.product.ReserveQuantitySucceededEvent;
import org.atlas.framework.event.handler.EventHandler;
import org.atlas.framework.event.publisher.OrderEventPublisherPort;
import org.atlas.framework.exception.BusinessException;
import org.atlas.framework.objectmapper.ObjectMapperUtil;

@RequiredArgsConstructor
public class ReserveQuantitySucceededEventHandler implements
    EventHandler<ReserveQuantitySucceededEvent> {

  private final OrderRepository orderRepository;
  private final ApplicationConfigPort applicationConfigPort;
  private final OrderEventPublisherPort orderEventPublisherPort;

  @Override
  public void handle(ReserveQuantitySucceededEvent reserveQuantitySucceededEvent) {
    // Find order
    OrderEntity orderEntity = orderRepository.findById(reserveQuantitySucceededEvent.getOrderId())
        .orElseThrow(() -> new BusinessException(AppError.ORDER_NOT_FOUND));
    if (orderEntity.getStatus() != OrderStatus.PROCESSING) {
      throw new BusinessException(AppError.ORDER_INVALID_STATUS);
    }

    // Update order
    orderEntity.setStatus(OrderStatus.CONFIRMED);
    orderRepository.update(orderEntity);

    // Publish event
    OrderConfirmedEvent orderConfirmedEvent = new OrderConfirmedEvent(
        applicationConfigPort.getApplicationName());
    ObjectMapperUtil.getInstance()
        .merge(reserveQuantitySucceededEvent, orderConfirmedEvent);
    orderEventPublisherPort.publish(orderConfirmedEvent);
  }
}
