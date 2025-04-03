package org.atlas.port.inbound.notification.event;

import org.atlas.platform.event.contract.order.OrderCanceledEvent;

public interface OrderCanceledEventHandler {

  void handle(OrderCanceledEvent event);
}
