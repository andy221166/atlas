package org.atlas.framework.domain.event.handler;

import org.atlas.framework.domain.event.DomainEvent;

public interface EventHandler<E extends DomainEvent> {

  void handle(E event);
}
