package org.atlas.infrastructure.event.kafka.adapter.order.publisher;

import lombok.RequiredArgsConstructor;
import org.atlas.framework.event.contract.order.OrderCanceledEvent;
import org.atlas.framework.event.contract.order.OrderConfirmedEvent;
import org.atlas.framework.event.contract.order.OrderCreatedEvent;
import org.atlas.framework.event.publisher.OrderEventPublisherPort;
import org.atlas.infrastructure.event.gateway.EventGateway;
import org.atlas.infrastructure.event.kafka.core.KafkaEventProps;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisherAdapter implements OrderEventPublisherPort {

  private final EventGateway eventGateway;
  private final KafkaEventProps kafkaEventProps;

  @Override
  public void publish(OrderCreatedEvent event) {
    eventGateway.send(event, kafkaEventProps.getTopic().getOrderCreatedEvent(),
        String.valueOf(event.getOrderId()));
  }

  @Override
  public void publish(OrderConfirmedEvent event) {
    eventGateway.send(event, kafkaEventProps.getTopic().getOrderConfirmedEvent(),
        String.valueOf(event.getOrderId()));
  }

  @Override
  public void publish(OrderCanceledEvent event) {
    eventGateway.send(event, kafkaEventProps.getTopic().getOrderCanceledEvent(),
        String.valueOf(event.getOrderId()));
  }
}
