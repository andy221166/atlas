package org.atlas.infrastructure.event.sns.adapter.notification.consumer;

import lombok.extern.slf4j.Slf4j;
import org.atlas.domain.notification.event.OrderCanceledEventHandler;
import org.atlas.framework.event.DomainEvent;
import org.atlas.framework.event.contract.order.OrderCanceledEvent;
import org.atlas.infrastructure.event.sns.core.SnsEventConsumer;
import org.atlas.infrastructure.event.sns.core.SnsEventProps;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@Slf4j
public class OrderCanceledEventConsumer extends SnsEventConsumer implements InitializingBean {

  private final OrderCanceledEventHandler orderCanceledEventHandler;

  public OrderCanceledEventConsumer(SnsEventProps snsEventProps,
      SqsClient sqsClient,
      OrderCanceledEventHandler orderCanceledEventHandler) {
    super(snsEventProps, sqsClient);
    this.orderCanceledEventHandler = orderCanceledEventHandler;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    consumeMessages("order-canceled-event",
        snsEventProps.getSqsQueueUrl().getOrderCanceledEvent());
  }

  @Override
  protected void handleEvent(DomainEvent event) {
    orderCanceledEventHandler.handle((OrderCanceledEvent) event);
  }
}
