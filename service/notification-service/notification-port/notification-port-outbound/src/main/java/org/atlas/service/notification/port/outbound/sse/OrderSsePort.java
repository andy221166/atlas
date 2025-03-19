package org.atlas.service.notification.port.outbound.sse;

import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;

public interface OrderSsePort {

  void notify(OrderConfirmedEvent event);

  void notify(OrderCanceledEvent event);
}
