package org.atlas.service.notification.adapter.event.sns.consumer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.sns.SnsEventConsumer;
import org.atlas.platform.event.sns.SnsEventProps;
import org.atlas.service.notification.port.inbound.event.OrderCanceledEventHandler;
import org.atlas.service.notification.port.inbound.event.OrderConfirmedEventHandler;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@Slf4j
public class OrderEventsConsumer extends SnsEventConsumer {

  private final OrderConfirmedEventHandler orderConfirmedEventHandler;
  private final OrderCanceledEventHandler orderCanceledEventHandler;

  public OrderEventsConsumer(SnsEventProps snsEventProps,
      SqsClient sqsClient, OrderConfirmedEventHandler orderConfirmedEventHandler,
      OrderCanceledEventHandler orderCanceledEventHandler) {
    super(snsEventProps, sqsClient);
    this.orderConfirmedEventHandler = orderConfirmedEventHandler;
    this.orderCanceledEventHandler = orderCanceledEventHandler;
  }

  @PostConstruct
  public void init() {
    consumeMessages("order-events", snsEventProps.getSqsQueueUrl().getOrderEvents());
  }

  @PreDestroy
  public void destroy() {
    shutdown();
  }

  @Override
  protected void handleEvent(DomainEvent event) {
    EventType eventType = EventType.findEventType(event.getClass());
    if (eventType.equals(EventType.ORDER_CONFIRMED)) {
      orderConfirmedEventHandler.handle((OrderConfirmedEvent) event);
    } else if (eventType.equals(EventType.ORDER_CANCELED)) {
      orderCanceledEventHandler.handle((OrderCanceledEvent) event);
    } else {
      log.debug("Ignoring unsupported event type: {}", eventType);
    }
  }
}
