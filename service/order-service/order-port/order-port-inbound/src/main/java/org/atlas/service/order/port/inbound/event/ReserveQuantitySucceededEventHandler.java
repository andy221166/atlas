package org.atlas.service.order.port.inbound.event;

import org.atlas.platform.event.contract.product.ReserveQuantitySucceededEvent;

public interface ReserveQuantitySucceededEventHandler {

  void handle(ReserveQuantitySucceededEvent event);
}
