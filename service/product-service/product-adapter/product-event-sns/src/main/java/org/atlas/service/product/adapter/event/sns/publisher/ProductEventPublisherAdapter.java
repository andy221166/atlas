package org.atlas.service.product.adapter.event.sns.publisher;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.product.ProductCreatedEvent;
import org.atlas.platform.event.contract.product.ProductDeletedEvent;
import org.atlas.platform.event.contract.product.ProductUpdatedEvent;
import org.atlas.platform.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.platform.event.contract.product.ReserveQuantitySucceededEvent;
import org.atlas.platform.event.gateway.EventGateway;
import org.atlas.platform.event.sns.SnsEventProps;
import org.atlas.service.product.port.outbound.event.ProductEventPublisherPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventPublisherAdapter implements ProductEventPublisherPort {

  private final EventGateway eventGateway;
  private final SnsEventProps snsEventProps;

  @Override
  public void publish(ProductCreatedEvent event) {
    eventGateway.send(event, snsEventProps.getSnsTopicArn().getProductEvents());
  }

  @Override
  public void publish(ProductUpdatedEvent event) {
    eventGateway.send(event, snsEventProps.getSnsTopicArn().getProductEvents());
  }

  @Override
  public void publish(ProductDeletedEvent event) {
    eventGateway.send(event, snsEventProps.getSnsTopicArn().getProductEvents());
  }

  @Override
  public void publish(ReserveQuantitySucceededEvent event) {
    eventGateway.send(event, snsEventProps.getSnsTopicArn().getOrderEvents());
  }

  @Override
  public void publish(ReserveQuantityFailedEvent event) {
    eventGateway.send(event, snsEventProps.getSnsTopicArn().getOrderEvents());
  }
}
