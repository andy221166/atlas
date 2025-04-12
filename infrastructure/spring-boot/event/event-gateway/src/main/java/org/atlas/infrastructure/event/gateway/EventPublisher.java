package org.atlas.infrastructure.event.gateway;

import org.atlas.framework.event.DomainEvent;

public interface EventPublisher {

  void publish(DomainEvent event, String destination, String messageKey);
}
