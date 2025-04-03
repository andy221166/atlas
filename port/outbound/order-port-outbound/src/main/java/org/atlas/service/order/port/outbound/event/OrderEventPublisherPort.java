package org.atlas.service.order.port.outbound.event;

import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;

public interface OrderEventPublisherPort {

  void publish(OrderCreatedEvent event);

  void publish(OrderConfirmedEvent event);

  void publish(OrderCanceledEvent event);
}
