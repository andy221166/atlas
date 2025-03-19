package org.atlas.service.notification.port.outbound.email;

import org.atlas.platform.event.contract.order.OrderConfirmedEvent;

public interface OrderEmailPort {

  void notify(OrderConfirmedEvent event);
}
