package org.atlas.infrastructure.event.gateway.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.domain.outbox.entity.OutboxMessageEntity;
import org.atlas.domain.outbox.entity.OutboxMessageStatus;
import org.atlas.domain.outbox.repository.OutboxMessageRepository;
import org.atlas.framework.event.DomainEvent;
import org.atlas.framework.event.EventType;
import org.atlas.framework.json.JsonUtil;
import org.atlas.infrastructure.event.gateway.EventGateway;
import org.atlas.infrastructure.event.gateway.EventPublisher;
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
    OutboxMessageEntity outboxMessage = newOutboxMessage(event, destination);
    outboxMessageRepository.insert(outboxMessage);
    log.info("Inserted outbox message {} of event {}", outboxMessage.getId(), event);

    try {
      // Parse event JSON string to object
      eventPublisher.publish(event, destination);

      // Mark outbox message as be processed
      outboxMessage.markAsProcessed();
      outboxMessageRepository.insert(outboxMessage);
    } catch (Exception e) {
      log.error("Failed to process outbox message {} of event {} {}",
          outboxMessage.getId(), outboxMessage.getEventType(), outboxMessage.getEventJson(), e);
    }
  }

  private OutboxMessageEntity newOutboxMessage(DomainEvent event, String destination) {
    OutboxMessageEntity outboxMessage = new OutboxMessageEntity();
    outboxMessage.setEventJson(JsonUtil.getInstance().toJson(event));
    outboxMessage.setEventType(EventType.findEventType(event.getClass()));
    outboxMessage.setDestination(destination);
    outboxMessage.setStatus(OutboxMessageStatus.PENDING);
    outboxMessage.setRetries(0);
    return outboxMessage;
  }
}
