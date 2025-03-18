package org.atlas.service.notification.application.event.websocket;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.notification.websocket.order.OrderStatusNotificationWsService;
import org.atlas.service.notification.application.event.model.OrderStatusOutput;
import org.atlas.service.notification.port.inbound.event.OrderCanceledEventHandler;
import org.atlas.service.order.domain.shared.OrderStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WsOrderCanceledEventHandlerImpl implements OrderCanceledEventHandler {

  private final OrderStatusNotificationWsService wsService;

  @Override
  public void handle(OrderCanceledEvent event) {
    OrderStatusOutput payload = new OrderStatusOutput(OrderStatus.CANCELED,
        event.getCanceledReason());
    wsService.notify(event.getOrderId(), payload);
  }
}
