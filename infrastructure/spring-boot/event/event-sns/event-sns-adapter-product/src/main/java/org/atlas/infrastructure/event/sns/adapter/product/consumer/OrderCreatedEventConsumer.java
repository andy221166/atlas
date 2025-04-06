package org.atlas.infrastructure.event.sns.adapter.product.consumer;

import lombok.extern.slf4j.Slf4j;
import org.atlas.domain.product.event.OrderCreatedEventHandler;
import org.atlas.framework.event.DomainEvent;
import org.atlas.framework.event.contract.order.OrderCreatedEvent;
import org.atlas.infrastructure.event.sns.core.SnsEventConsumer;
import org.atlas.infrastructure.event.sns.core.SnsEventProps;
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
