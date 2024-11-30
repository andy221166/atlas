package org.atlas.service.order.application.handler.event.choreography;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.core.consumer.EventHandler;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.contract.order.choreography.CreditReservedEvent;
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
public class CreditReservedEventHandler implements EventHandler<CreditReservedEvent> {

  private final OrderRepository orderRepository;
  private final OrderService orderService;
  private final EventPublisherTemplate eventPublisherTemplate;

  @Override
  public EventType supports() {
    return EventType.CREDIT_RESERVED;
  }

  @Override
  @Transactional
  public void handle(CreditReservedEvent creditReservedEvent) {
    OrderDto orderDto = creditReservedEvent.getOrder();
    Order order = orderService.findProcessingOrder(orderDto.getId());
    order.setStatus(OrderStatus.CONFIRMED);
    orderRepository.update(order);

    orderDto.setStatus(OrderStatus.CONFIRMED);
    OrderConfirmedEvent orderConfirmedEvent = new OrderConfirmedEvent(orderDto);
    eventPublisherTemplate.publish(orderConfirmedEvent);
  }
}
