package org.atlas.infrastructure.event.gateway;

import lombok.RequiredArgsConstructor;
import org.atlas.framework.event.DomainEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultEventGateway implements EventGateway {

  private final EventPublisher eventPublisher;

  @Override
  public void send(DomainEvent event, String destination) {
    eventPublisher.publish(event, destination);
  }
}
