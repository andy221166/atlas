package org.atlas.service.user.adapter.event.kafka.publisher;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.user.UserRegisteredEvent;
import org.atlas.platform.event.gateway.EventGateway;
import org.atlas.platform.event.kafka.TopicsProps;
import org.atlas.service.user.port.outbound.event.UserEventPublisherPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventPublisherAdapter implements UserEventPublisherPort {

  private final EventGateway eventGateway;
  private final TopicsProps topicsProps;

  @Override
  public void publish(UserRegisteredEvent event) {
    eventGateway.send(event, topicsProps.getUserEvents());
  }
}
