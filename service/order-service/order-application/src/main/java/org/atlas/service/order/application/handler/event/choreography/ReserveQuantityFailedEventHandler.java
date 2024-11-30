package org.atlas.service.order.application.handler.event.choreography;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.core.consumer.EventHandler;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.choreography.ReserveQuantityFailedEvent;
import org.atlas.platform.event.core.publisher.EventPublisherTemplate;
import org.atlas.service.order.application.service.OrderService;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.order.contract.repository.OrderRepository;
import org.atlas.service.order.domain.Order;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReserveQuantityFailedEventHandler implements EventHandler<ReserveQuantityFailedEvent> {

  private final OrderRepository orderRepository;
  private final OrderService orderService;
  private final EventPublisherTemplate eventPublisherTemplate;

  @Override
  public EventType supports() {
    return EventType.RESERVE_QUANTITY_FAILED;
  }

  @Override
  @Transactional
  public void handle(ReserveQuantityFailedEvent event) {
    OrderDto orderDto = event.getOrder();
    Order order = orderService.findProcessingOrder(orderDto.getId());
    order.setStatus(OrderStatus.CANCELED);
    order.setCanceledReason(event.getError());
    orderRepository.update(order);

    orderDto.setStatus(OrderStatus.CANCELED);
    orderDto.setCanceledReason(event.getError());
    OrderCanceledEvent orderCanceledEvent = new OrderCanceledEvent(orderDto);
    eventPublisherTemplate.publish(orderCanceledEvent);
  }
}
