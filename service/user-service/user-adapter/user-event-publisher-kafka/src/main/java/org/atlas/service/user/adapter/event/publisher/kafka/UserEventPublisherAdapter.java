package org.atlas.service.user.adapter.event.publisher.kafka;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.core.EventGateway;
import org.atlas.platform.event.publisher.kafka.KafkaEventPublisherProps;
import org.atlas.service.user.domain.event.UserRegisteredEvent;
import org.atlas.service.user.port.outbound.event.publisher.UserEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventPublisherAdapter implements UserEventPublisher {

  private final EventGateway eventGateway;
  private final KafkaEventPublisherProps kafkaEventPublisherProps;

  @Override
  public void publish(UserRegisteredEvent event) {
    eventGateway.send(event, kafkaEventPublisherProps.getTopics().getUserEvents());
  }
}
