package org.atlas.service.order.adapter.event.sns.consumer;

import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.product.ReserveQuantitySucceededEvent;
import org.atlas.platform.event.sns.SnsEventConsumer;
import org.atlas.platform.event.sns.SnsEventProps;
import org.atlas.service.order.port.inbound.event.ReserveQuantitySucceededEventHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@Slf4j
public class ReserveQuantitySucceededEventConsumer extends SnsEventConsumer
    implements InitializingBean {

  private final ReserveQuantitySucceededEventHandler reserveQuantitySucceededEventHandler;

  public ReserveQuantitySucceededEventConsumer(SnsEventProps snsEventProps,
      SqsClient sqsClient,
      ReserveQuantitySucceededEventHandler reserveQuantitySucceededEventHandler) {
    super(snsEventProps, sqsClient);
    this.reserveQuantitySucceededEventHandler = reserveQuantitySucceededEventHandler;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    consumeMessages("reserve-quantity-succeeded-event",
        snsEventProps.getSqsQueueUrl().getReserveQuantitySucceededEvent());
  }

  @Override
  protected void handleEvent(DomainEvent event) {
    reserveQuantitySucceededEventHandler.handle((ReserveQuantitySucceededEvent) event);
  }
}
