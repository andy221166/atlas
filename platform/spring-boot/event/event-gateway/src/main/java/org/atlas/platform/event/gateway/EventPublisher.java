package org.atlas.platform.event.gateway;

import org.atlas.platform.event.contract.DomainEvent;

public interface EventPublisher {

  void publish(DomainEvent event, String destination);
}
