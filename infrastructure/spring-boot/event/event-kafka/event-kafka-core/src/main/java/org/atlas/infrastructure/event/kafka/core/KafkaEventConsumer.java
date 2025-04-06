package org.atlas.infrastructure.event.kafka.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.atlas.framework.event.DomainEvent;

@Slf4j
public abstract class KafkaEventConsumer {

  protected void consumeMessage(ConsumerRecord<String, DomainEvent> record) {
    log.info("Consumed record: payload={}, partition={}, offset={}",
        record.value(), record.partition(), record.offset());
    DomainEvent event = record.value();
    handleEvent(event);
  }

  protected abstract void handleEvent(DomainEvent event);
}
