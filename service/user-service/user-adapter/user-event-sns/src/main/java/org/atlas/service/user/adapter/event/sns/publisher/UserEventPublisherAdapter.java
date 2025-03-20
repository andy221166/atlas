package org.atlas.service.user.adapter.event.sns.publisher;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.user.UserRegisteredEvent;
import org.atlas.platform.event.gateway.EventGateway;
import org.atlas.platform.event.sns.SnsEventProps;
import org.atlas.service.user.port.outbound.event.UserEventPublisherPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventPublisherAdapter implements UserEventPublisherPort {

  private final EventGateway eventGateway;
  private final SnsEventProps snsEventProps;

  @Override
  public void publish(UserRegisteredEvent event) {
    eventGateway.send(event, snsEventProps.getSnsTopicArn().getUserEvents());
  }
}
