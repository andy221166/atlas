package org.atlas.service.product.adapter.event.publisher.kafka;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.core.EventGateway;
import org.atlas.platform.event.publisher.kafka.KafkaEventPublisherProps;
import org.atlas.platform.event.contract.product.ProductCreatedEvent;
import org.atlas.platform.event.contract.product.ProductDeletedEvent;
import org.atlas.platform.event.contract.product.ProductUpdatedEvent;
import org.atlas.platform.event.contract.order.ReserveQuantityFailedEvent;
import org.atlas.platform.event.contract.order.ReserveQuantitySucceededEvent;
import org.atlas.service.product.port.outbound.event.publisher.ProductEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventPublisherAdapter implements ProductEventPublisher {

  private final EventGateway eventGateway;
  private final KafkaEventPublisherProps kafkaEventPublisherProps;

  @Override
  public void publish(ProductCreatedEvent event) {
    eventGateway.send(event, kafkaEventPublisherProps.getTopics().getProductEvents());
  }

  @Override
  public void publish(ProductUpdatedEvent event) {
    eventGateway.send(event, kafkaEventPublisherProps.getTopics().getProductEvents());
  }

  @Override
  public void publish(ProductDeletedEvent event) {
    eventGateway.send(event, kafkaEventPublisherProps.getTopics().getProductEvents());
  }

  @Override
  public void publish(ReserveQuantitySucceededEvent event) {
    eventGateway.send(event, kafkaEventPublisherProps.getTopics().getOrderEvents());
  }

  @Override
  public void publish(ReserveQuantityFailedEvent event) {
    eventGateway.send(event, kafkaEventPublisherProps.getTopics().getOrderEvents());
  }
}
