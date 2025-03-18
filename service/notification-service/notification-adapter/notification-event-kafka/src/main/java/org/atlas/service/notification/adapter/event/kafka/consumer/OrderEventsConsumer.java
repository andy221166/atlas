package org.atlas.service.notification.adapter.event.kafka.consumer;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.kafka.BaseKafkaEventConsumer;
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
public class OrderEventsConsumer extends BaseKafkaEventConsumer {

  private final List<OrderConfirmedEventHandler> orderConfirmedEventHandlers;
  private final List<OrderCanceledEventHandler> orderCanceledEventHandlers;

  @Override
  @KafkaListener(
      topics = "#{topicsProps.orderEvents}",
      containerFactory = "defaultContainerFactory"
  )
  // Non-blocking retry
  @RetryableTopic(
      attempts = "4", // max retries is 3
      topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE, // order-retry-0, order-retry-1, order-retry-2, etc.
      backoff = @Backoff(delay = 1000, multiplier = 2, random = true) // Exponential backoff
  )
  public void consume(ConsumerRecord<String, DomainEvent> record) {
    super.consume(record);
  }

  @Override
  protected void handle(DomainEvent event) {
    if (event.getEventType().equals(EventType.ORDER_CONFIRMED)) {
      orderConfirmedEventHandlers.forEach(
          handler -> handler.handle((OrderConfirmedEvent) event));
    } else if (event.getEventType().equals(EventType.ORDER_CANCELED)) {
      orderCanceledEventHandlers.forEach(
          handler -> handler.handle((OrderCanceledEvent) event));
    } else {
      log.debug("Ignoring unsupported event type: {}", event.getEventType());
    }
  }
}
