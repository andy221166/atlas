package org.atlas.platform.event.core;

import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.EventType;

public interface EventHandler<T extends DomainEvent> {

  EventType supports();

  void handle(T event);
}
