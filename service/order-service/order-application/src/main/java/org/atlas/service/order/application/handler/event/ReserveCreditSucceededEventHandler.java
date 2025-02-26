package org.atlas.service.order.application.handler.event;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.event.EventType;
import org.atlas.platform.cqrs.event.EventHandler;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.contract.order.ReserveCreditSucceededEvent;
import org.atlas.platform.event.core.publisher.DefaultEventPublisher;
import org.atlas.service.order.application.service.OrderService;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.order.contract.repository.OrderRepository;
import org.atlas.service.order.domain.Order;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReserveCreditSucceededEventHandler implements
    EventHandler<ReserveCreditSucceededEvent> {

  private final OrderRepository orderRepository;
  private final OrderService orderService;
  private final DefaultEventPublisher eventPublisherTemplate;

  @Override
  public EventType supports() {
    return EventType.RESERVE_CREDIT_SUCCEEDED;
  }

  @Override
  @Transactional
  public void handle(ReserveCreditSucceededEvent reserveCreditSucceededEvent) {
    OrderDto orderDto = reserveCreditSucceededEvent.getOrder();
    Order order = orderService.findProcessingOrder(orderDto.getId());
    order.setStatus(OrderStatus.CONFIRMED);
    orderRepository.update(order);

    orderDto.setStatus(OrderStatus.CONFIRMED);
    OrderConfirmedEvent orderConfirmedEvent = new OrderConfirmedEvent(orderDto);
    eventPublisherTemplate.publish(orderConfirmedEvent);
  }
}
