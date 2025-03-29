package org.atlas.service.product.adapter.event.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.platform.event.kafka.KafkaEventConsumer;
import org.atlas.service.product.port.inbound.event.OrderCreatedEventHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventConsumer extends KafkaEventConsumer {

  private final OrderCreatedEventHandler orderCreatedEventHandler;

  @Override
  @KafkaListener(
      topics = "#{kafkaEventProps.topic.orderCreatedEvent}",
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
    orderCreatedEventHandler.handle((OrderCreatedEvent) event);
  }
}
