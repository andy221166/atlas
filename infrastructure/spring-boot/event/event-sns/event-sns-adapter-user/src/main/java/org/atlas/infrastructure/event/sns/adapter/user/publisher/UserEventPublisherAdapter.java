package org.atlas.infrastructure.event.sns.adapter.user.publisher;

import lombok.RequiredArgsConstructor;
import org.atlas.framework.event.contract.user.UserRegisteredEvent;
import org.atlas.framework.event.publisher.UserEventPublisherPort;
import org.atlas.infrastructure.event.gateway.EventGateway;
import org.atlas.infrastructure.event.sns.core.SnsEventProps;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventPublisherAdapter implements UserEventPublisherPort {

  private final EventGateway eventGateway;
  private final SnsEventProps snsEventProps;

  @Override
  public void publish(UserRegisteredEvent event) {
    eventGateway.send(event, snsEventProps.getSnsTopicArn().getUserRegisteredEvent());
  }
}
