package org.atlas.service.notification.adapter.event.sns.consumer;

import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.sns.SnsEventConsumer;
import org.atlas.platform.event.sns.SnsEventProps;
import org.atlas.service.notification.port.inbound.event.OrderConfirmedEventHandler;
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
