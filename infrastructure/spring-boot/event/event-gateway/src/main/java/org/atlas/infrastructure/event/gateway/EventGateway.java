package org.atlas.infrastructure.event.gateway;

import org.atlas.framework.event.DomainEvent;

public interface EventGateway {

  void send(DomainEvent event, String destination);
}
