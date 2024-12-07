package org.atlas.platform.event.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.order.BaseOrderEvent;
import org.atlas.platform.event.core.consumer.EventDispatcher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventConsumer {

  private final EventDispatcher eventDispatcher;

  @KafkaListener(
      topics = "${app.event.kafka.order-topic}",
      groupId = "${spring.application.name}",
      containerFactory = "defaultContainerFactory"
  )
  // Non-blocking retry
  @RetryableTopic(
      attempts = "4", // max retries is 3
      topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE, // order-retry-0, order-retry-1, order-retry-2, etc.
      backoff = @Backoff(delay = 1000, multiplier = 2, random = true) // Exponential backoff
  )
  public <E extends BaseOrderEvent> void consumeOrderEvent(ConsumerRecord<String, E> record) {
    log.info("Consumed record: payload={}, partition={}, offset={}",
        record.value(), record.partition(), record.offset());
    DomainEvent event = record.value();
    eventDispatcher.dispatch(event);
  }
}
