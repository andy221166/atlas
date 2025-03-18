package org.atlas.platform.event.gateway;

import org.atlas.platform.event.contract.DomainEvent;

public interface EventGateway {

  void send(DomainEvent event, String destination);
}
