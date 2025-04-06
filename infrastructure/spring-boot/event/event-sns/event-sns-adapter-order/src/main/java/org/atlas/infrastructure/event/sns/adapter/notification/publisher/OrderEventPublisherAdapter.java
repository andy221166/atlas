package org.atlas.infrastructure.event.sns.adapter.notification.publisher;

import lombok.RequiredArgsConstructor;
import org.atlas.framework.event.contract.order.OrderCanceledEvent;
import org.atlas.framework.event.contract.order.OrderConfirmedEvent;
import org.atlas.framework.event.contract.order.OrderCreatedEvent;
import org.atlas.framework.event.publisher.OrderEventPublisherPort;
import org.atlas.infrastructure.event.gateway.EventGateway;
import org.atlas.infrastructure.event.sns.core.SnsEventProps;
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
