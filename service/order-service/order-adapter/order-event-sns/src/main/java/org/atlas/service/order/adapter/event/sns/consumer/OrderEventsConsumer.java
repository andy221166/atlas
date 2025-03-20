package org.atlas.service.order.adapter.event.sns.consumer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.platform.event.contract.product.ReserveQuantitySucceededEvent;
import org.atlas.platform.event.sns.SnsEventConsumer;
import org.atlas.platform.event.sns.SnsEventProps;
import org.atlas.service.order.port.inbound.event.ReserveQuantityFailedEventHandler;
import org.atlas.service.order.port.inbound.event.ReserveQuantitySucceededEventHandler;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@Slf4j
public class OrderEventsConsumer extends SnsEventConsumer {

  private final ReserveQuantitySucceededEventHandler reserveQuantitySucceededEventHandler;
  private final ReserveQuantityFailedEventHandler reserveQuantityFailedEventHandler;

  public OrderEventsConsumer(SnsEventProps snsEventProps,
      SqsClient sqsClient,
      ReserveQuantitySucceededEventHandler reserveQuantitySucceededEventHandler,
      ReserveQuantityFailedEventHandler reserveQuantityFailedEventHandler) {
    super(snsEventProps, sqsClient);
    this.reserveQuantitySucceededEventHandler = reserveQuantitySucceededEventHandler;
    this.reserveQuantityFailedEventHandler = reserveQuantityFailedEventHandler;
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
    if (event.getEventType().equals(EventType.RESERVE_QUANTITY_SUCCEEDED)) {
      reserveQuantitySucceededEventHandler.handle((ReserveQuantitySucceededEvent) event);
    } else if (event.getEventType().equals(EventType.RESERVE_QUANTITY_FAILED)) {
      reserveQuantityFailedEventHandler.handle((ReserveQuantityFailedEvent) event);
    } else {
      log.debug("Ignoring unsupported event type: {}", event.getEventType());
    }
  }
}
