package org.atlas.service.product.adapter.event.sns.consumer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.platform.event.sns.SnsEventConsumer;
import org.atlas.platform.event.sns.SnsEventProps;
import org.atlas.service.product.port.inbound.event.OrderCreatedEventHandler;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@Slf4j
public class OrderCreatedEventConsumer extends SnsEventConsumer {

  private final OrderCreatedEventHandler orderCreatedEventHandler;

  public OrderCreatedEventConsumer(SnsEventProps snsEventProps,
      SqsClient sqsClient,
      OrderCreatedEventHandler orderCreatedEventHandler) {
    super(snsEventProps, sqsClient);
    this.orderCreatedEventHandler = orderCreatedEventHandler;
  }

  @PostConstruct
  public void init() {
    consumeMessages("order-created-event", snsEventProps.getSqsQueueUrl().getOrderCreatedEvent());
  }

  @PreDestroy
  public void destroy() {
    shutdown();
  }

  @Override
  protected void handleEvent(DomainEvent event) {
    orderCreatedEventHandler.handle((OrderCreatedEvent) event);
  }
}
