package org.atlas.infrastructure.event.kafka.adapter.order.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.atlas.domain.order.event.ReserveQuantityFailedEventHandler;
import org.atlas.framework.event.DomainEvent;
import org.atlas.framework.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.infrastructure.event.kafka.core.KafkaEventConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReserveQuantityFailedEventConsumer extends KafkaEventConsumer {

  private final ReserveQuantityFailedEventHandler reserveQuantityFailedEventHandler;

  @Override
  @KafkaListener(
      topics = "#{kafkaEventProps.topic.reserveQuantityFailedEvent}",
      containerFactory = "defaultContainerFactory"
  )
  // Non-blocking retry
  @RetryableTopic(
      attempts = "4", // max retries is 3
      topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE, // order-events-retry-0, order-events-retry-1, order-events-retry-2, etc.
      backoff = @Backoff(delay = 1000, multiplier = 2, random = true) // Exponential backoff
  )
  public void consumeMessage(ConsumerRecord<String, DomainEvent> record) {
    super.consumeMessage(record);
  }

  @Override
  protected void handleEvent(DomainEvent event) {
    reserveQuantityFailedEventHandler.handle((ReserveQuantityFailedEvent) event);
  }
}
