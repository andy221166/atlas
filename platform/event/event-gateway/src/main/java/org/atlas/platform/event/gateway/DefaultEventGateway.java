package org.atlas.platform.event.gateway;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.DomainEvent;
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
