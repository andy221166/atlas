package org.atlas.port.inbound.notification.event;

import org.atlas.platform.event.contract.order.OrderConfirmedEvent;

public interface OrderConfirmedEventHandler {

  void handle(OrderConfirmedEvent event);
}
