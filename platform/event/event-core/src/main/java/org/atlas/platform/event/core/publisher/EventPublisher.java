package org.atlas.platform.event.core.publisher;

import org.atlas.platform.event.contract.DomainEvent;

public interface EventPublisher {

  <E extends DomainEvent> void publish(E event);
}
