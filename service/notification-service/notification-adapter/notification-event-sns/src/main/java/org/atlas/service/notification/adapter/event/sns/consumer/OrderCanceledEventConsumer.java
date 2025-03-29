package org.atlas.service.notification.adapter.event.sns.consumer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.sns.SnsEventConsumer;
import org.atlas.platform.event.sns.SnsEventProps;
import org.atlas.service.notification.port.inbound.event.OrderCanceledEventHandler;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@Slf4j
public class OrderCanceledEventConsumer extends SnsEventConsumer {

  private final OrderCanceledEventHandler orderCanceledEventHandler;

  public OrderCanceledEventConsumer(SnsEventProps snsEventProps,
      SqsClient sqsClient,
      OrderCanceledEventHandler orderCanceledEventHandler) {
    super(snsEventProps, sqsClient);
    this.orderCanceledEventHandler = orderCanceledEventHandler;
  }

  @PostConstruct
  public void init() {
    consumeMessages("order-canceled-event",
        snsEventProps.getSqsQueueUrl().getOrderCanceledEvent());
  }

  @PreDestroy
  public void destroy() {
    shutdown();
  }

  @Override
  protected void handleEvent(DomainEvent event) {
    orderCanceledEventHandler.handle((OrderCanceledEvent) event);
  }
}
