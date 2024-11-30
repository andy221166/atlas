package org.atlas.platform.event.core.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.util.json.JsonUtil;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.EventTypeMapper;
import org.atlas.platform.outbox.model.OutboxMessage;
import org.atlas.platform.outbox.model.OutboxMessageStatus;
import org.atlas.platform.outbox.repository.OutboxMessageRepository;
import org.atlas.platform.outbox.service.OutboxMessageService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisherTemplate {

  private final OutboxMessageRepository outboxMessageRepository;
  private final OutboxMessageService outboxMessageService;

  @Transactional
  public <E extends DomainEvent> void publish(E event) {
    OutboxMessage outboxMessage = newOutboxMessage(event);
    outboxMessageRepository.insert(outboxMessage);

    // Create new transaction to isolate this action.
    // If occurs exception, a scheduled job will recover it later.
    outboxMessageService.process(outboxMessage);
  }

  private <E extends DomainEvent> OutboxMessage newOutboxMessage(E event) {
    OutboxMessage outboxMessage = new OutboxMessage();
    outboxMessage.setEventType(EventTypeMapper.getEventType(event.getClass()));
    outboxMessage.setPayload(JsonUtil.toJson(event));
    outboxMessage.setStatus(OutboxMessageStatus.PENDING);
    outboxMessage.setRetries(0);
    return outboxMessage;
  }
}
