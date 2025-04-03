package org.atlas.port.inbound.order.event;

import org.atlas.platform.event.contract.product.ReserveQuantitySucceededEvent;

public interface ReserveQuantitySucceededEventHandler {

  void handle(ReserveQuantitySucceededEvent event);
}
