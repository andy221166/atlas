package org.atlas.service.order.application.event;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.service.order.application.service.OrderAggregator;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.order.port.outbound.repository.OrderRepository;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.atlas.service.order.domain.entity.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReserveQuantityFailedEventHandler implements EventHandler<ReserveCreditFailedEvent> {

  private final OrderRepository orderRepository;
  private final OrderAggregator orderAggregator;
  private final DefaultEventPublisher eventPublisherTemplate;

  @Override
  public EventType supports() {
    return EventType.RESERVE_CREDIT_FAILED;
  }

  @Override
  @Transactional
  public void handle(ReserveCreditFailedEvent event) {
    OrderDto orderDto = event.getOrder();
    OrderEntity orderEntity = orderAggregator.findProcessingOrder(orderDto.getId());
    orderEntity.setStatus(OrderStatus.CANCELED);
    orderEntity.setCanceledReason(event.getError());
    orderRepository.update(orderEntity);

    orderDto.setStatus(OrderStatus.CANCELED);
    orderDto.setCanceledReason(event.getError());
    OrderCanceledEvent orderCanceledEvent = new OrderCanceledEvent(orderDto);
    eventPublisherTemplate.publish(orderCanceledEvent);
  }
}
