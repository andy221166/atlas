package org.atlas.platform.event.core.consumer;

import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.EventType;

public interface EventHandler<E extends DomainEvent> {

  EventType supports();

  void handle(E event);
}
