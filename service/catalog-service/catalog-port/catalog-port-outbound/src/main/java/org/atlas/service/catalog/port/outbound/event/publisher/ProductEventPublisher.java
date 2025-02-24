package org.atlas.service.catalog.port.outbound.event.publisher;

import org.atlas.service.catalog.domain.event.ProductCreatedEvent;
import org.atlas.service.catalog.domain.event.ProductDeletedEvent;
import org.atlas.service.catalog.domain.event.ProductUpdatedEvent;

public interface ProductEventPublisher {

  void publish(ProductCreatedEvent event);
  void publish(ProductUpdatedEvent event);
  void publish(ProductDeletedEvent event);
}
