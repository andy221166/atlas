package org.atlas.infrastructure.event.kafka.core;

import io.micrometer.context.ContextSnapshot;
import io.micrometer.context.ContextSnapshotFactory;
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
  private final ContextSnapshotFactory contextSnapshotFactory;

  @Override
  public void publish(DomainEvent event, String topic, String messageKey) {
    if (StringUtils.isBlank(topic)) {
      throw new IllegalArgumentException("Topic must be specified");
    }

    // Capture MDC context before async operation
    ContextSnapshot snapshot = contextSnapshotFactory.captureAll();

    // Asynchronous send
    kafkaTemplate.send(topic, messageKey, event)
        .whenCompleteAsync((result, throwable) -> {
          // Restore context (including MDC) in the callback
          try (ContextSnapshot.Scope scope = snapshot.setThreadLocals()) {
            if (throwable == null) {
              log.info("Published event: {}\nTopic: {}. Partition: {}. Offset: {}",
                  event, topic, result.getRecordMetadata().partition(),
                  result.getRecordMetadata().offset());
            } else {
              log.error("Failed to publish event: {}\nTopic: {}. Error: {}",
                  event, topic, throwable.getMessage(), throwable);
            }
          }
        });
  }
}
