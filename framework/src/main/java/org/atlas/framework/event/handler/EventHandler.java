package org.atlas.framework.event.handler;

import org.atlas.framework.event.DomainEvent;

public interface EventHandler<E extends DomainEvent> {

  void handle(E event);
}
