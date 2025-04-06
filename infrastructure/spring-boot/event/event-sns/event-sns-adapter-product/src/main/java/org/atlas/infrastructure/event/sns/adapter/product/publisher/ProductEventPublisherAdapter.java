package org.atlas.infrastructure.event.sns.adapter.product.publisher;

import lombok.RequiredArgsConstructor;
import org.atlas.framework.event.contract.product.ProductCreatedEvent;
import org.atlas.framework.event.contract.product.ProductDeletedEvent;
import org.atlas.framework.event.contract.product.ProductUpdatedEvent;
import org.atlas.framework.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.framework.event.contract.product.ReserveQuantitySucceededEvent;
import org.atlas.framework.event.publisher.ProductEventPublisherPort;
import org.atlas.infrastructure.event.gateway.EventGateway;
import org.atlas.infrastructure.event.sns.core.SnsEventProps;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventPublisherAdapter implements ProductEventPublisherPort {

  private final EventGateway eventGateway;
  private final SnsEventProps snsEventProps;

  @Override
  public void publish(ProductCreatedEvent event) {
    eventGateway.send(event, snsEventProps.getSnsTopicArn().getProductCreatedEvent());
  }

  @Override
  public void publish(ProductUpdatedEvent event) {
    eventGateway.send(event, snsEventProps.getSnsTopicArn().getProductUpdatedEvent());
  }

  @Override
  public void publish(ProductDeletedEvent event) {
    eventGateway.send(event, snsEventProps.getSnsTopicArn().getProductDeletedEvent());
  }

  @Override
  public void publish(ReserveQuantitySucceededEvent event) {
    eventGateway.send(event, snsEventProps.getSnsTopicArn().getReserveQuantitySucceededEvent());
  }

  @Override
  public void publish(ReserveQuantityFailedEvent event) {
    eventGateway.send(event, snsEventProps.getSnsTopicArn().getReserveQuantityFailedEvent());
  }
}
