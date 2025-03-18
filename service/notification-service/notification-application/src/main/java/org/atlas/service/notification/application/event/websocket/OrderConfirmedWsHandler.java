package org.atlas.service.notification.application.event.websocket;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.gateway.consumer.EventHandler;
import org.atlas.platform.event.gateway.model.EventType;
import org.atlas.platform.notification.websocket.order.OrderStatusNotificationWsService;
import org.atlas.service.notification.application.event.model.OrderStatusOutput;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConfirmedWsHandler implements EventHandler<OrderConfirmedEvent> {

  private final OrderStatusNotificationWsService wsService;

  @Override
  public EventType supports() {
    return EventType.ORDER_CONFIRMED;
  }

  @Override
  public void handle(OrderConfirmedEvent event) {
    OrderStatusOutput payload = new OrderStatusOutput(OrderStatus.CONFIRMED);
    wsService.notify(event.getOrderPayload().getId(), payload);
  }
}
