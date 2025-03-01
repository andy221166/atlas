package org.atlas.service.order.port.outbound.event.publisher;

import org.atlas.platform.event.contract.order.OrderCreatedEvent;

public interface OrderEventPublisher {

  void publish(OrderCreatedEvent event);
}
