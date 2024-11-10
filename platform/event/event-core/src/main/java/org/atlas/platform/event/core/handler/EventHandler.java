package org.atlas.platform.event.core.handler;

import org.atlas.platform.event.core.DomainEvent;
import org.atlas.platform.event.core.EventType;

public interface EventHandler<E extends DomainEvent> {

    EventType supports();

    void handle(E event);
}
