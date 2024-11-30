package org.atlas.service.order.application.handler.event.orchestration;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.contract.order.orchestration.ReserveCreditReplyEvent;
import org.atlas.platform.event.core.consumer.EventHandler;
import org.atlas.platform.event.core.publisher.EventPublisherTemplate;
import org.atlas.service.order.application.service.OrderService;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.order.contract.repository.OrderRepository;
import org.atlas.service.order.domain.Order;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReserveCreditReplyEventHandler implements EventHandler<ReserveCreditReplyEvent> {

  private final OrderRepository orderRepository;
  private final OrderService orderService;
  private final EventPublisherTemplate eventPublisherTemplate;

  @Override
  public EventType supports() {
    return EventType.RESERVE_CREDIT_REPLY;
  }

  @Override
  public void handle(ReserveCreditReplyEvent reserveCreditReplyEvent) {
    OrderDto orderDto = reserveCreditReplyEvent.getOrder();
    Order order = orderService.findProcessingOrder(orderDto.getId());
    if (reserveCreditReplyEvent.isSuccess()) {
      order.setStatus(OrderStatus.CONFIRMED);
      orderRepository.update(order);

      orderDto.setStatus(OrderStatus.CONFIRMED);
      OrderConfirmedEvent orderConfirmedEvent = new OrderConfirmedEvent(orderDto);
      eventPublisherTemplate.publish(orderConfirmedEvent);
    } else {
      order.setStatus(OrderStatus.CANCELED);
      order.setCanceledReason(reserveCreditReplyEvent.getError());
      orderRepository.update(order);

      orderDto.setStatus(OrderStatus.CANCELED);
      orderDto.setCanceledReason(reserveCreditReplyEvent.getError());
      OrderCanceledEvent orderCanceledEvent = new OrderCanceledEvent(orderDto);
      eventPublisherTemplate.publish(orderCanceledEvent);
    }
  }
}
