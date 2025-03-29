package org.atlas.service.order.adapter.event.sns.consumer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.product.ReserveQuantitySucceededEvent;
import org.atlas.platform.event.sns.SnsEventConsumer;
import org.atlas.platform.event.sns.SnsEventProps;
import org.atlas.service.order.port.inbound.event.ReserveQuantitySucceededEventHandler;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@Slf4j
public class ReserveQuantitySucceededEventConsumer extends SnsEventConsumer {

  private final ReserveQuantitySucceededEventHandler reserveQuantitySucceededEventHandler;

  public ReserveQuantitySucceededEventConsumer(SnsEventProps snsEventProps,
      SqsClient sqsClient,
      ReserveQuantitySucceededEventHandler reserveQuantitySucceededEventHandler) {
    super(snsEventProps, sqsClient);
    this.reserveQuantitySucceededEventHandler = reserveQuantitySucceededEventHandler;
  }

  @PostConstruct
  public void init() {
    consumeMessages("reserve-quantity-succeeded-event",
        snsEventProps.getSqsQueueUrl().getReserveQuantitySucceededEvent());
  }

  @PreDestroy
  public void destroy() {
    shutdown();
  }

  @Override
  protected void handleEvent(DomainEvent event) {
    reserveQuantitySucceededEventHandler.handle((ReserveQuantitySucceededEvent) event);
  }
}
