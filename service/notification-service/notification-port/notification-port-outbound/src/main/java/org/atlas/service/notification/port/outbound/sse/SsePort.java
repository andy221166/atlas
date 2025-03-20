package org.atlas.service.notification.port.outbound.sse;

import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;

public interface SsePort {

  void notify(OrderCanceledEvent event);
}
