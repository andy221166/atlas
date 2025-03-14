package org.atlas.service.order.application.event;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.contract.order.ReserveQuantitySucceededEvent;
import org.atlas.platform.event.core.EventHandler;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.atlas.service.order.port.outbound.event.publisher.OrderEventPublisher;
import org.atlas.service.order.port.outbound.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReserveQuantitySucceededEventHandler implements
    EventHandler<ReserveQuantitySucceededEvent> {

  private final OrderRepository orderRepository;
  private final OrderEventPublisher orderEventPublisher;

  @Override
  public EventType supports() {
    return EventType.RESERVE_QUANTITY_SUCCEEDED;
  }

  @Override
  @Transactional
  public void handle(ReserveQuantitySucceededEvent reserveQuantitySucceededEvent) {
    OrderEntity orderEntity = orderAggregator.findProcessingOrder(reserveQuantitySucceededEvent.getOrderId());
    orderEntity.setStatus(OrderStatus.CONFIRMED);
    orderRepository.update(orderEntity);

    OrderConfirmedEvent orderConfirmedEvent = new OrderConfirmedEvent(orderDto);
    eventPublisherTemplate.publish(orderConfirmedEvent);
  }
}
