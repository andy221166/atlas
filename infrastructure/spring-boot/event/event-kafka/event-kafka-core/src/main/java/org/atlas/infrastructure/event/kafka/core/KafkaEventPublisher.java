package org.atlas.infrastructure.event.kafka.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.atlas.framework.event.DomainEvent;
import org.atlas.infrastructure.event.gateway.EventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void publish(DomainEvent event, String topic) {
    if (StringUtils.isBlank(topic)) {
      throw new IllegalArgumentException("Topic must be specified");
    }
    kafkaTemplate.send(topic, event).whenCompleteAsync(
        (result, throwable) -> {
          if (throwable == null) {
            log.info("Published event: {}\nTopic: {}. Partition: {}. Offset: {}",
                event, topic, result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset());
          } else {
            log.error("Failed to publish event: {}\nTopic: {}.",
                event, topic, throwable);
          }
        });
  }
}
