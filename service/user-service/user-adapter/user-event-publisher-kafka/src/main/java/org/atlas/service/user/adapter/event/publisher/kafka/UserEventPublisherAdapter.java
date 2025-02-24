package org.atlas.service.user.adapter.event.publisher.kafka;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.publisher.kafka.KafkaEventPublisher;
import org.atlas.platform.event.publisher.kafka.KafkaEventPublisherProps;
import org.atlas.service.user.domain.event.UserRegisteredEvent;
import org.atlas.service.user.port.outbound.event.publisher.UserEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventPublisherAdapter implements UserEventPublisher {

  private final KafkaEventPublisher kafkaEventPublisher;
  private final KafkaEventPublisherProps kafkaEventPublisherProps;

  @Override
  public void publish(UserRegisteredEvent event) {
    kafkaEventPublisher.publish(event, kafkaEventPublisherProps.getTopics().getUserEvents());
  }
}
