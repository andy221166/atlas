package org.atlas.infrastructure.event.gateway.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.event.DomainEvent;
import org.atlas.framework.event.EventType;
import org.atlas.infrastructure.event.gateway.EventGateway;
import org.atlas.infrastructure.event.gateway.EventPublisher;
import org.atlas.infrastructure.event.gateway.outbox.entity.OutboxMessage;
import org.atlas.infrastructure.event.gateway.outbox.entity.OutboxMessageStatus;
import org.atlas.infrastructure.event.gateway.outbox.repository.OutboxMessageRepository;
import org.atlas.infrastructure.json.JsonUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class OutboxEventGateway implements EventGateway {

  private final OutboxMessageRepository outboxMessageRepository;
  private final EventPublisher eventPublisher;

  @Override
  public void send(DomainEvent event, String destination) {
    OutboxMessage outboxMessage = newOutboxMessage(event, destination);
    outboxMessageRepository.insert(outboxMessage);
    log.info("Inserted outbox message {} of event {}", outboxMessage.getId(), event);

    try {
      // Parse event JSON string to object
      eventPublisher.publish(event, destination);

      // Mark outbox message as be processed
      outboxMessage.markAsProcessed();
      outboxMessageRepository.save(outboxMessage);
    } catch (Exception e) {
      log.error("Failed to process outbox message {} of event {} {}",
          outboxMessage.getId(), outboxMessage.getEventType(), outboxMessage.getEventJson(), e);
    }
  }

  private OutboxMessage newOutboxMessage(DomainEvent event, String destination) {
    OutboxMessage outboxMessage = new OutboxMessage();
    outboxMessage.setEventJson(JsonUtil.getInstance().toJson(event));
    outboxMessage.setEventType(EventType.findEventType(event.getClass()));
    outboxMessage.setDestination(destination);
    outboxMessage.setStatus(OutboxMessageStatus.PENDING);
    outboxMessage.setRetries(0);
    return outboxMessage;
  }
}
