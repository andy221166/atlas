package org.atlas.platform.event.rabbitmq.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.contract.EventTypeMapper;
import org.atlas.platform.event.core.publisher.EventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitmqEventPublisher implements EventPublisher {

  @Value("${app.event.rabbitmq.publisher.order-exchange}")
  private String orderExchange;

  private final RabbitTemplate rabbitTemplate;

  @Override
  public void publish(DomainEvent event) {
    EventType eventType = EventTypeMapper.getEventType(event.getClass());
    // Queue and routing key have the same name
    String routingKey = eventType.name().toLowerCase();
    rabbitTemplate.convertAndSend(orderExchange, routingKey, event);
  }
}
