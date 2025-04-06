package org.atlas.infrastructure.event.gateway;

import lombok.RequiredArgsConstructor;
import org.atlas.framework.event.DomainEvent;
import org.atlas.infrastructure.event.gateway.outbox.service.OutboxMessageService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class OutboxEventGateway implements EventGateway {

  private final OutboxMessageService outboxMessageService;

  @Override
  public void send(DomainEvent event, String destination) {
    outboxMessageService.insertOutboxMessage(event, destination);
  }
}
