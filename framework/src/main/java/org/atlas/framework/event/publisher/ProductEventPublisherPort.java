package org.atlas.framework.event.publisher;

import org.atlas.framework.event.contract.product.ProductCreatedEvent;
import org.atlas.framework.event.contract.product.ProductDeletedEvent;
import org.atlas.framework.event.contract.product.ProductUpdatedEvent;
import org.atlas.framework.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.framework.event.contract.product.ReserveQuantitySucceededEvent;

public interface ProductEventPublisherPort {

  void publish(ProductCreatedEvent event);

  void publish(ProductUpdatedEvent event);

  void publish(ProductDeletedEvent event);

  void publish(ReserveQuantitySucceededEvent event);

  void publish(ReserveQuantityFailedEvent event);
}
