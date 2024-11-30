package org.atlas.service.order.application.handler.event.orchestration;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.core.consumer.EventHandler;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.orchestration.ReserveCreditRequestEvent;
import org.atlas.platform.event.contract.order.orchestration.ReserveQuantityReplyEvent;
import org.atlas.platform.event.core.publisher.EventPublisherTemplate;
import org.atlas.service.order.application.service.OrderService;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.order.contract.repository.OrderRepository;
import org.atlas.service.order.domain.Order;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReserveQuantityReplyEventHandler implements EventHandler<ReserveQuantityReplyEvent> {

  private final OrderRepository orderRepository;
  private final OrderService orderService;
  private final EventPublisherTemplate eventPublisherTemplate;

  @Override
  public EventType supports() {
    return EventType.RESERVE_QUANTITY_REPLY;
  }

  @Override
  public void handle(ReserveQuantityReplyEvent reserveQuantityReplyEvent) {
    OrderDto orderDto = reserveQuantityReplyEvent.getOrder();
    Order order = orderService.findProcessingOrder(orderDto.getId());
    if (reserveQuantityReplyEvent.isSuccess()) {
      ReserveCreditRequestEvent reserveCreditRequestEvent = new ReserveCreditRequestEvent(orderDto);
      eventPublisherTemplate.publish(reserveCreditRequestEvent);
    } else {
      order.setStatus(OrderStatus.CANCELED);
      order.setCanceledReason(reserveQuantityReplyEvent.getError());
      orderRepository.update(order);

      orderDto.setStatus(OrderStatus.CANCELED);
      orderDto.setCanceledReason(reserveQuantityReplyEvent.getError());
      OrderCanceledEvent orderCanceledEvent = new OrderCanceledEvent(orderDto);
      eventPublisherTemplate.publish(orderCanceledEvent);
    }
  }
}
