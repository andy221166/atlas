package org.atlas.platform.event.kafka.publisher;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.order.BaseOrderEvent;
import org.atlas.platform.event.core.publisher.EventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

  @Value("${app.event.kafka.order-topic}")
  private String orderTopic;

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void publish(DomainEvent event) {
    String topic = getTopic(event);
    CompletableFuture<SendResult<String, Object>> completableFuture = kafkaTemplate.send(topic,
        event);
    completableFuture.whenComplete((result, e) -> {
      if (e == null) {
        log.info("Published event: {}\nTopic: {}. Partition: {}. Offset: {}",
            event, topic, result.getRecordMetadata().partition(),
            result.getRecordMetadata().offset());
      } else {
        log.error("Failed to publish event: {}\nTopic: {}.",
            event, topic, e);
      }
    });
  }

  private String getTopic(DomainEvent event) {
    if (event instanceof BaseOrderEvent) {
      return orderTopic;
    }
    throw new IllegalArgumentException("Unsupported event type: " + event.getEventType());
  }
}
