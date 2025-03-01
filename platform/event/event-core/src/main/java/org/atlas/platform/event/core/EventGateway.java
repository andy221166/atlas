package org.atlas.platform.event.core;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.core.outbox.service.OutboxMessageService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventGateway {

  private final OutboxMessageService outboxMessageService;

  public void send(DomainEvent event, String destination) {
    outboxMessageService.insertOutboxMessage(event, destination);
  }
}
