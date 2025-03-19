package org.atlas.service.notification.port.outbound.websocket;

import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;

public interface OrderWebSocketPort {

  void notify(OrderConfirmedEvent event);

  void notify(OrderCanceledEvent event);
}
