package org.atlas.port.inbound.product.event;

import org.atlas.platform.event.contract.order.OrderCreatedEvent;

public interface OrderCreatedEventHandler {

  void handle(OrderCreatedEvent event);
}
