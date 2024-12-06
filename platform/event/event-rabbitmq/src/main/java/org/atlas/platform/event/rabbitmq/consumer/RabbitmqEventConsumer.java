package org.atlas.platform.event.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.core.consumer.EventDispatcher;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitmqEventConsumer {

  private final EventDispatcher eventDispatcher;

  @RabbitListener(
      queues = "#{T(org.atlas.commons.util.base.StringUtil).split('${app.event.rabbitmq.consumer.queues}',',')}",
      containerFactory = "customContainerFactory"
  )
  public void consume(Message<DomainEvent> message) {
    DomainEvent event = message.getPayload();
    MessageHeaders headers = message.getHeaders();
    log.info("Received message: payload={}, headers={}", event, headers);
    eventDispatcher.dispatch(event);
  }
}
