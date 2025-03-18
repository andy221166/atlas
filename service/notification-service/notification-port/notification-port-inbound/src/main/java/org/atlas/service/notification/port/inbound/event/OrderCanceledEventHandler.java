package org.atlas.service.notification.port.inbound.event;

import org.atlas.platform.event.contract.order.OrderCanceledEvent;

public interface OrderCanceledEventHandler {

  void handle(OrderCanceledEvent event);
}
