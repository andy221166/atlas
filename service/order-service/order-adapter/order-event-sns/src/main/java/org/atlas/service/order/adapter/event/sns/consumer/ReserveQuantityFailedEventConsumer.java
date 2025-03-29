package org.atlas.service.order.adapter.event.sns.consumer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.platform.event.sns.SnsEventConsumer;
import org.atlas.platform.event.sns.SnsEventProps;
import org.atlas.service.order.port.inbound.event.ReserveQuantityFailedEventHandler;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@Slf4j
public class ReserveQuantityFailedEventConsumer extends SnsEventConsumer {

  private final ReserveQuantityFailedEventHandler reserveQuantityFailedEventHandler;

  public ReserveQuantityFailedEventConsumer(SnsEventProps snsEventProps,
      SqsClient sqsClient,
      ReserveQuantityFailedEventHandler reserveQuantityFailedEventHandler) {
    super(snsEventProps, sqsClient);
    this.reserveQuantityFailedEventHandler = reserveQuantityFailedEventHandler;
  }

  @PostConstruct
  public void init() {
    consumeMessages("reserve-quantity-failed-event", snsEventProps.getSqsQueueUrl().getReserveQuantityFailedEvent());
  }

  @PreDestroy
  public void destroy() {
    shutdown();
  }

  @Override
  protected void handleEvent(DomainEvent event) {
    reserveQuantityFailedEventHandler.handle((ReserveQuantityFailedEvent) event);
  }
}
