package org.atlas.platform.event.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.atlas.platform.event.contract.DomainEvent;

@Slf4j
public abstract class BaseKafkaEventConsumer {

  protected void consume(ConsumerRecord<String, DomainEvent> record) {
    log.info("Consumed record: payload={}, partition={}, offset={}",
        record.value(), record.partition(), record.offset());
    DomainEvent event = record.value();
    handle(event);
  }

  protected abstract void handle(DomainEvent event);
}
