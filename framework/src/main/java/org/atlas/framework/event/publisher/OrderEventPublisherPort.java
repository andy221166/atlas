package org.atlas.framework.event.publisher;

import org.atlas.framework.event.contract.order.OrderCanceledEvent;
import org.atlas.framework.event.contract.order.OrderConfirmedEvent;
import org.atlas.framework.event.contract.order.OrderCreatedEvent;

public interface OrderEventPublisherPort {

  void publish(OrderCreatedEvent event);

  void publish(OrderConfirmedEvent event);

  void publish(OrderCanceledEvent event);
}
