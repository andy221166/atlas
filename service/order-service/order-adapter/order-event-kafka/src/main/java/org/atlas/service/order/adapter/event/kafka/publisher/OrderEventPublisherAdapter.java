package org.atlas.service.order.adapter.event.kafka.publisher;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.platform.event.gateway.EventGateway;
import org.atlas.platform.event.kafka.KafkaEventProps;
import org.atlas.service.order.port.outbound.event.OrderEventPublisherPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisherAdapter implements OrderEventPublisherPort {

  private final EventGateway eventGateway;
  private final KafkaEventProps kafkaEventProps;

  @Override
  public void publish(OrderCreatedEvent event) {
    eventGateway.send(event, kafkaEventProps.getTopic().getOrderEvents());
  }

  @Override
  public void publish(OrderConfirmedEvent event) {
    eventGateway.send(event, kafkaEventProps.getTopic().getOrderEvents());
  }

  @Override
  public void publish(OrderCanceledEvent event) {
    eventGateway.send(event, kafkaEventProps.getTopic().getOrderEvents());
  }
}
