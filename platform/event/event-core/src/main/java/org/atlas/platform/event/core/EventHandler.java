package org.atlas.platform.event.core;

import org.atlas.platform.commons.event.DomainEvent;
import org.atlas.platform.commons.event.EventType;

public interface EventHandler {

  EventType supports();

  void handle(DomainEvent event);
}
