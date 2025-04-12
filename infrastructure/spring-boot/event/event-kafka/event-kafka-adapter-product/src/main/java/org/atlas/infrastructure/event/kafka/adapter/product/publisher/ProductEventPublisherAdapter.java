package org.atlas.infrastructure.event.kafka.adapter.product.publisher;

import lombok.RequiredArgsConstructor;
import org.atlas.framework.event.contract.product.ProductCreatedEvent;
import org.atlas.framework.event.contract.product.ProductDeletedEvent;
import org.atlas.framework.event.contract.product.ProductUpdatedEvent;
import org.atlas.framework.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.framework.event.contract.product.ReserveQuantitySucceededEvent;
import org.atlas.framework.event.publisher.ProductEventPublisherPort;
import org.atlas.infrastructure.event.gateway.EventGateway;
import org.atlas.infrastructure.event.kafka.core.KafkaEventProps;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventPublisherAdapter implements ProductEventPublisherPort {

  private final EventGateway eventGateway;
  private final KafkaEventProps kafkaEventProps;

  @Override
  public void publish(ProductCreatedEvent event) {
    eventGateway.send(event, kafkaEventProps.getTopic().getProductCreatedEvent(),
        String.valueOf(event.getProductId()));
  }

  @Override
  public void publish(ProductUpdatedEvent event) {
    eventGateway.send(event, kafkaEventProps.getTopic().getProductUpdatedEvent(),
        String.valueOf(event.getProductId()));
  }

  @Override
  public void publish(ProductDeletedEvent event) {
    eventGateway.send(event, kafkaEventProps.getTopic().getProductDeletedEvent(),
        String.valueOf(event.getProductId()));
  }

  @Override
  public void publish(ReserveQuantitySucceededEvent event) {
    eventGateway.send(event, kafkaEventProps.getTopic().getReserveQuantitySucceededEvent(),
        String.valueOf(event.getOrderId()));
  }

  @Override
  public void publish(ReserveQuantityFailedEvent event) {
    eventGateway.send(event, kafkaEventProps.getTopic().getReserveQuantityFailedEvent(),
        String.valueOf(event.getOrderId()));
  }
}
