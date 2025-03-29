package org.atlas.service.order.adapter.event.sns.publisher;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.platform.event.gateway.EventGateway;
import org.atlas.platform.event.sns.SnsEventProps;
import org.atlas.service.order.port.outbound.event.OrderEventPublisherPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisherAdapter implements OrderEventPublisherPort {

  private final EventGateway eventGateway;
  private final SnsEventProps snsEventProps;

  @Override
  public void publish(OrderCreatedEvent event) {
    eventGateway.send(event, snsEventProps.getSnsTopicArn().getOrderCreatedEvent());
  }

  @Override
  public void publish(OrderConfirmedEvent event) {
    eventGateway.send(event, snsEventProps.getSnsTopicArn().getOrderConfirmedEvent());
  }

  @Override
  public void publish(OrderCanceledEvent event) {
    eventGateway.send(event, snsEventProps.getSnsTopicArn().getOrderCanceledEvent());
  }
}
