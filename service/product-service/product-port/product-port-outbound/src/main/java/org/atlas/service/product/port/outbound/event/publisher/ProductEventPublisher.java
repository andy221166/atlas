package org.atlas.service.product.port.outbound.event.publisher;

import org.atlas.platform.event.contract.order.ReserveQuantityFailedEvent;
import org.atlas.platform.event.contract.order.ReserveQuantitySucceededEvent;
import org.atlas.platform.event.contract.product.ProductCreatedEvent;
import org.atlas.platform.event.contract.product.ProductDeletedEvent;
import org.atlas.platform.event.contract.product.ProductUpdatedEvent;

public interface ProductEventPublisher {

  void publish(ProductCreatedEvent event);

  void publish(ProductUpdatedEvent event);

  void publish(ProductDeletedEvent event);

  void publish(ReserveQuantitySucceededEvent event);

  void publish(ReserveQuantityFailedEvent event);
}
