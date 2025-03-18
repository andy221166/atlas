package org.atlas.service.notification.application.event.websocket;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.notification.websocket.order.OrderStatusNotificationWsService;
import org.atlas.service.notification.application.event.model.OrderStatusOutput;
import org.atlas.service.notification.port.inbound.event.OrderConfirmedEventHandler;
import org.atlas.service.order.domain.shared.OrderStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WsOrderConfirmedEventHandlerImpl implements OrderConfirmedEventHandler {

  private final OrderStatusNotificationWsService wsService;

  @Override
  public void handle(OrderConfirmedEvent event) {
    OrderStatusOutput payload = new OrderStatusOutput(OrderStatus.CONFIRMED);
    wsService.notify(event.getOrderId(), payload);
  }
}
