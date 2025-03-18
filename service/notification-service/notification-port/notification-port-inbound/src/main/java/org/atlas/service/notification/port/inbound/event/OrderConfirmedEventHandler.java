package org.atlas.service.notification.port.inbound.event;

import org.atlas.platform.event.contract.order.OrderConfirmedEvent;

public interface OrderConfirmedEventHandler {

  void handle(OrderConfirmedEvent event);
}
