package org.atlas.infrastructure.event.sns.adapter.notification.consumer;

import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.event.DomainEvent;
import org.atlas.framework.event.contract.order.OrderConfirmedEvent;
import org.atlas.infrastructure.event.sns.core.SnsEventConsumer;
import org.atlas.infrastructure.event.sns.core.SnsEventProps;
import org.atlas.port.inbound.notification.event.OrderConfirmedEventHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@Slf4j
public class OrderConfirmedEventConsumer extends SnsEventConsumer implements InitializingBean {

  private final OrderConfirmedEventHandler orderConfirmedEventHandler;

  public OrderConfirmedEventConsumer(SnsEventProps snsEventProps,
      SqsClient sqsClient,
      OrderConfirmedEventHandler orderConfirmedEventHandler) {
    super(snsEventProps, sqsClient);
    this.orderConfirmedEventHandler = orderConfirmedEventHandler;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    consumeMessages("order-confirmed-event",
        snsEventProps.getSqsQueueUrl().getOrderConfirmedEvent());
  }

  @Override
  protected void handleEvent(DomainEvent event) {
    orderConfirmedEventHandler.handle((OrderConfirmedEvent) event);
  }
}
