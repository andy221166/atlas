package org.atlas.service.aggregator.application.handler.event;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.core.consumer.EventHandler;
import org.atlas.service.aggregator.contract.repository.aggregator.AggOrderRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(value = "app.aggregator", havingValue = "event")
@RequiredArgsConstructor
public class OrderConfirmedHandler implements EventHandler<OrderConfirmedEvent> {

  private final AggOrderRepository aggOrderRepository;

  @Override
  public EventType supports() {
    return EventType.ORDER_CONFIRMED;
  }

  @Override
  @Transactional
  public void handle(OrderConfirmedEvent event) {
    AggOrder aggOrder = OrderPayloadConverter.convert(event.getOrderPayload());
    aggOrderRepository.insert(aggOrder);
  }
}
