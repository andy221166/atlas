package org.atlas.infrastructure.event.sns.adapter.notification.consumer;

import lombok.extern.slf4j.Slf4j;
import org.atlas.domain.order.event.ReserveQuantityFailedEventHandler;
import org.atlas.framework.event.DomainEvent;
import org.atlas.framework.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.infrastructure.event.sns.core.SnsEventConsumer;
import org.atlas.infrastructure.event.sns.core.SnsEventProps;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@Slf4j
public class ReserveQuantityFailedEventConsumer extends SnsEventConsumer
    implements InitializingBean {

  private final ReserveQuantityFailedEventHandler reserveQuantityFailedEventHandler;

  public ReserveQuantityFailedEventConsumer(SnsEventProps snsEventProps,
      SqsClient sqsClient,
      ReserveQuantityFailedEventHandler reserveQuantityFailedEventHandler) {
    super(snsEventProps, sqsClient);
    this.reserveQuantityFailedEventHandler = reserveQuantityFailedEventHandler;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    consumeMessages("reserve-quantity-failed-event",
        snsEventProps.getSqsQueueUrl().getReserveQuantityFailedEvent());
  }

  @Override
  protected void handleEvent(DomainEvent event) {
    reserveQuantityFailedEventHandler.handle((ReserveQuantityFailedEvent) event);
  }
}
