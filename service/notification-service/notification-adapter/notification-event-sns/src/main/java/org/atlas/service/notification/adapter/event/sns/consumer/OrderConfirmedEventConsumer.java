package org.atlas.service.notification.adapter.event.sns.consumer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.sns.SnsEventConsumer;
import org.atlas.platform.event.sns.SnsEventProps;
import org.atlas.service.notification.port.inbound.event.OrderConfirmedEventHandler;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@Slf4j
public class OrderConfirmedEventConsumer extends SnsEventConsumer {

  private final OrderConfirmedEventHandler orderConfirmedEventHandler;

  public OrderConfirmedEventConsumer(SnsEventProps snsEventProps,
      SqsClient sqsClient,
      OrderConfirmedEventHandler orderConfirmedEventHandler) {
    super(snsEventProps, sqsClient);
    this.orderConfirmedEventHandler = orderConfirmedEventHandler;
  }

  @PostConstruct
  public void init() {
    consumeMessages("order-confirmed-event",
        snsEventProps.getSqsQueueUrl().getOrderConfirmedEvent());
  }

  @PreDestroy
  public void destroy() {
    shutdown();
  }

  @Override
  protected void handleEvent(DomainEvent event) {
    orderConfirmedEventHandler.handle((OrderConfirmedEvent) event);
  }
}
