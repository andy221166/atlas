package org.atlas.service.product.adapter.event.sns.consumer;

import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.platform.event.sns.SnsEventConsumer;
import org.atlas.platform.event.sns.SnsEventProps;
import org.atlas.port.inbound.product.event.OrderCreatedEventHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@Slf4j
public class OrderCreatedEventConsumer extends SnsEventConsumer implements InitializingBean {

  private final OrderCreatedEventHandler orderCreatedEventHandler;

  public OrderCreatedEventConsumer(SnsEventProps snsEventProps,
      SqsClient sqsClient,
      OrderCreatedEventHandler orderCreatedEventHandler) {
    super(snsEventProps, sqsClient);
    this.orderCreatedEventHandler = orderCreatedEventHandler;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    consumeMessages("order-created-event",
        snsEventProps.getSqsQueueUrl().getOrderCreatedEvent());
  }

  @Override
  protected void handleEvent(DomainEvent event) {
    orderCreatedEventHandler.handle((OrderCreatedEvent) event);
  }
}
