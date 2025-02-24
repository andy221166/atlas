package org.atlas.platform.event.core;

import org.atlas.platform.commons.event.DomainEvent;

public interface EventPublisher {

  void publish(DomainEvent event, String destination);
}
