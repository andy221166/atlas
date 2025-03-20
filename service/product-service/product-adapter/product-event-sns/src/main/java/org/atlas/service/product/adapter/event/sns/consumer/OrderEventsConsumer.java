package org.atlas.service.product.adapter.event.sns.consumer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.platform.event.sns.SnsEventConsumer;
import org.atlas.platform.event.sns.SnsEventProps;
import org.atlas.service.product.port.inbound.event.OrderCreatedEventHandler;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@Slf4j
public class OrderEventsConsumer extends SnsEventConsumer {

  private final OrderCreatedEventHandler orderCreatedEventHandler;

  public OrderEventsConsumer(SnsEventProps snsEventProps,
      SqsClient sqsClient,
      OrderCreatedEventHandler orderCreatedEventHandler) {
    super(snsEventProps, sqsClient);
    this.orderCreatedEventHandler = orderCreatedEventHandler;
  }

  @PostConstruct
  public void init() {
    consumeMessages("order_events", snsEventProps.getSqsQueueUrl().getOrderEvents());
  }

  @PreDestroy
  public void destroy() {
    shutdown();
  }

  @Override
  protected void handleEvent(DomainEvent event) {
    if (event.getEventType().equals(EventType.ORDER_CREATED)) {
      orderCreatedEventHandler.handle((OrderCreatedEvent) event);
    } else {
      log.debug("Ignoring unsupported event type: {}", event.getEventType());
    }
  }
}
