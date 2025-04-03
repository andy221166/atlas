package org.atlas.port.inbound.order.event;

import org.atlas.platform.event.contract.product.ReserveQuantityFailedEvent;

public interface ReserveQuantityFailedEventHandler {

  void handle(ReserveQuantityFailedEvent event);
}
