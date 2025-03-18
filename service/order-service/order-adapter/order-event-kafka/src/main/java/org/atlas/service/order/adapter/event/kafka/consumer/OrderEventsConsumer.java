package org.atlas.service.order.adapter.event.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.platform.event.contract.product.ReserveQuantitySucceededEvent;
import org.atlas.platform.event.kafka.BaseKafkaEventConsumer;
import org.atlas.service.order.port.inbound.event.ReserveQuantityFailedEventHandler;
import org.atlas.service.order.port.inbound.event.ReserveQuantitySucceededEventHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventsConsumer extends BaseKafkaEventConsumer {

  private final ReserveQuantitySucceededEventHandler reserveQuantitySucceededEventHandler;
  private final ReserveQuantityFailedEventHandler reserveQuantityFailedEventHandler;

  @Override
  @KafkaListener(
      topics = "#{topicsProps.orderEvents}",
      groupId = "${spring.application.name}",
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
    if (event.getEventType().equals(EventType.RESERVE_QUANTITY_SUCCEEDED)) {
      reserveQuantitySucceededEventHandler.handle((ReserveQuantitySucceededEvent) event);
    } else if (event.getEventType().equals(EventType.RESERVE_QUANTITY_FAILED)) {
      reserveQuantityFailedEventHandler.handle((ReserveQuantityFailedEvent) event);
    } else {
      log.debug("Ignoring unsupported event type: {}", event.getEventType());
    }
  }
}
