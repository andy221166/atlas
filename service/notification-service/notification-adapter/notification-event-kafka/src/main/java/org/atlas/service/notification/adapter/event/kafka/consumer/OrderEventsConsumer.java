package org.atlas.service.notification.adapter.event.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.kafka.KafkaEventConsumer;
import org.atlas.service.notification.port.inbound.event.OrderCanceledEventHandler;
import org.atlas.service.notification.port.inbound.event.OrderConfirmedEventHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventsConsumer extends KafkaEventConsumer {

  private final OrderConfirmedEventHandler orderConfirmedEventHandler;
  private final OrderCanceledEventHandler orderCanceledEventHandler;

  @Override
  @KafkaListener(
      topics = "#{kafkaEventProps.topic.orderEvents}",
      containerFactory = "defaultContainerFactory"
  )
  // Non-blocking retry
  @RetryableTopic(
      attempts = "4", // max retries is 3
      topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE, // order-retry-0, order-retry-1, order-retry-2, etc.
      backoff = @Backoff(delay = 1000, multiplier = 2, random = true) // Exponential backoff
  )
  public void consumeMessage(ConsumerRecord<String, DomainEvent> record) {
    super.consumeMessage(record);
  }

  @Override
  protected void handleEvent(DomainEvent event) {
    if (event.getEventType().equals(EventType.ORDER_CONFIRMED)) {
      orderConfirmedEventHandler.handle((OrderConfirmedEvent) event);
    } else if (event.getEventType().equals(EventType.ORDER_CANCELED)) {
      orderCanceledEventHandler.handle((OrderCanceledEvent) event);
    } else {
      log.debug("Ignoring unsupported event type: {}", event.getEventType());
    }
  }
}
