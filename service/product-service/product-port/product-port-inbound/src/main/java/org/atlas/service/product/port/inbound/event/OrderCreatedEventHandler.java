package org.atlas.service.product.port.inbound.event;

import org.atlas.platform.event.contract.order.OrderCreatedEvent;

public interface OrderCreatedEventHandler {

  void handle(OrderCreatedEvent event);
}
