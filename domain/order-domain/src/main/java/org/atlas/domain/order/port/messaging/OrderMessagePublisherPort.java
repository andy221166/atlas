package org.atlas.domain.order.port.messaging;

import org.atlas.framework.event.contract.order.OrderCanceledEvent;
import org.atlas.framework.event.contract.order.OrderConfirmedEvent;
import org.atlas.framework.event.contract.order.OrderCreatedEvent;

public interface OrderMessagePublisherPort {

  void publish(OrderCreatedEvent event);

  void publish(OrderConfirmedEvent event);

  void publish(OrderCanceledEvent event);
}
