package org.atlas.service.order.adapter.event.kafka.publisher;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.platform.event.gateway.EventGateway;
import org.atlas.platform.event.publisher.kafka.KafkaEventPublisherProps;
import org.atlas.service.order.port.outbound.event.OrderEventPublisher;
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

  @Override
  public void publish(OrderConfirmedEvent event) {

  }

  @Override
  public void publish(OrderCanceledEvent event) {

  }
}
