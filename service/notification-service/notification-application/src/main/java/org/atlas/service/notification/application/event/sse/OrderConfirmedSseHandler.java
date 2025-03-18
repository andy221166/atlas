package org.atlas.service.notification.application.event.sse;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.gateway.consumer.EventHandler;
import org.atlas.platform.event.gateway.model.EventType;
import org.atlas.platform.notification.sse.order.OrderStatusNotificationSseService;
import org.atlas.service.notification.application.event.model.OrderStatusOutput;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConfirmedSseHandler implements EventHandler<OrderConfirmedEvent> {

  private final OrderStatusNotificationSseService sseService;

  @Override
  public EventType supports() {
    return EventType.ORDER_CONFIRMED;
  }

  @Override
  public void handle(OrderConfirmedEvent event) {
    OrderStatusOutput payload = new OrderStatusOutput(OrderStatus.CONFIRMED);
    sseService.notify(event.getOrderPayload().getId(), payload);
  }
}
