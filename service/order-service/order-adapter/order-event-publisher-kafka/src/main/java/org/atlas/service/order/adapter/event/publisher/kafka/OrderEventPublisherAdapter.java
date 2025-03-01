package org.atlas.service.order.adapter.event.publisher.kafka;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.platform.event.core.EventGateway;
import org.atlas.platform.event.publisher.kafka.KafkaEventPublisherProps;
import org.atlas.service.order.port.outbound.event.publisher.OrderEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisherAdapter implements OrderEventPublisher {

  private final EventGateway eventGateway;
  private final KafkaEventPublisherProps kafkaEventPublisherProps;

  @Override
  public void publish(OrderCreatedEvent event) {
    eventGateway.send(event, kafkaEventPublisherProps.getTopics().getOrderEvents());
  }
}
