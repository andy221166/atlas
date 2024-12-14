package org.atlas.platform.event.kafka.publisher;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.order.BaseOrderEvent;
import org.atlas.platform.event.core.publisher.EventPublisher;
import org.atlas.platform.event.kafka.config.KafkaEventProps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

  private final KafkaEventProps props;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void publish(DomainEvent event) {
    if (event instanceof BaseOrderEvent) {
      doPublish(event, props.getOrderTopic());
    } else {
      throw new IllegalArgumentException("Unsupported event type: " + event.getEventType());
    }
  }

  private void doPublish(DomainEvent event, String topic) {
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
