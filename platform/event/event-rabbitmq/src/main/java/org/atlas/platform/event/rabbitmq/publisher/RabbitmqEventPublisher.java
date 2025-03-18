package org.atlas.platform.event.rabbitmq.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.util.StringUtil;
import org.atlas.platform.event.gateway.model.DomainEvent;
import org.atlas.platform.event.gateway.publisher.ExternalEventPublisher;
import org.atlas.platform.event.rabbitmq.config.RabbitmqEventProps;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitmqEventPublisher implements ExternalEventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final RabbitmqEventProps props;

  @Override
  public void publish(DomainEvent event) {
    if (event instanceof BasePlaceOrderEvent) {
      doPublishToFanoutExchange(event, props.getOrderExchange());
    } else {
      throw new IllegalArgumentException("Unsupported event type: " + event.getEventType());
    }
  }

  private void doPublishToFanoutExchange(DomainEvent event, String exchange) {
    rabbitTemplate.convertAndSend(exchange, StringUtil.EMPTY, event);
  }
}
