package org.atlas.service.order.port.inbound.event;

import org.atlas.platform.event.contract.product.ReserveQuantityFailedEvent;

public interface ReserveQuantityFailedEventHandler {

  void handle(ReserveQuantityFailedEvent event);
}
