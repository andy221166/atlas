package org.atlas.service.order.application.event;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.contract.order.ReserveQuantitySucceededEvent;
import org.atlas.platform.event.core.EventHandler;
import org.atlas.service.order.application.service.OrderAggregator;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.order.port.outbound.repository.OrderRepository;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.atlas.service.order.domain.entity.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReserveQuantitySucceededEventHandler implements
    EventHandler<ReserveQuantitySucceededEvent> {

  private final OrderRepository orderRepository;
  private final OrderAggregator orderAggregator;
  private final DefaultEventPublisher eventPublisherTemplate;

  @Override
  public EventType supports() {
    return EventType.RESERVE_CREDIT_SUCCEEDED;
  }

  @Override
  @Transactional
  public void handle(ReserveCreditSucceededEvent reserveCreditSucceededEvent) {
    OrderDto orderDto = reserveCreditSucceededEvent.getOrder();
    OrderEntity orderEntity = orderAggregator.findProcessingOrder(orderDto.getId());
    orderEntity.setStatus(OrderStatus.CONFIRMED);
    orderRepository.update(orderEntity);

    orderDto.setStatus(OrderStatus.CONFIRMED);
    OrderConfirmedEvent orderConfirmedEvent = new OrderConfirmedEvent(orderDto);
    eventPublisherTemplate.publish(orderConfirmedEvent);
  }
}
