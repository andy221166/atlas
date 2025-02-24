package org.atlas.service.catalog.adapter.event.publisher.kafka;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.publisher.kafka.KafkaEventPublisher;
import org.atlas.platform.event.publisher.kafka.KafkaEventPublisherProps;
import org.atlas.service.catalog.domain.event.ProductCreatedEvent;
import org.atlas.service.catalog.domain.event.ProductDeletedEvent;
import org.atlas.service.catalog.domain.event.ProductUpdatedEvent;
import org.atlas.service.catalog.port.outbound.event.publisher.ProductEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventPublisherAdapter implements ProductEventPublisher {

  private final KafkaEventPublisher kafkaEventPublisher;
  private final KafkaEventPublisherProps kafkaEventPublisherProps;

  @Override
  public void publish(ProductCreatedEvent event) {
    kafkaEventPublisher.publish(event, kafkaEventPublisherProps.getTopics().getProductEvents());
  }

  @Override
  public void publish(ProductUpdatedEvent event) {
    kafkaEventPublisher.publish(event, kafkaEventPublisherProps.getTopics().getProductEvents());
  }

  @Override
  public void publish(ProductDeletedEvent event) {
    kafkaEventPublisher.publish(event, kafkaEventPublisherProps.getTopics().getProductEvents());
  }
}
