package org.atlas.platform.event.rabbitmq.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.order.BaseOrderEvent;
import org.atlas.platform.event.core.publisher.EventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitmqEventPublisher implements EventPublisher {

  @Value("${app.event.rabbitmq.order-exchange}")
  private String orderExchange;

  private final RabbitTemplate rabbitTemplate;

  @Override
  public void publish(DomainEvent event) {
    if (event instanceof BaseOrderEvent) {
      // Publish to the fanout exchange for order-related events
      rabbitTemplate.convertAndSend(orderExchange, StringUtils.EMPTY, event);
    } else {
      throw new IllegalArgumentException("Unsupported event type: " + event.getEventType());
    }
  }
}
