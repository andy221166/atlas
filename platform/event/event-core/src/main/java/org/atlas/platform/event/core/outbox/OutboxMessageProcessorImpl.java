package org.atlas.platform.event.core.outbox;

import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.json.JsonUtil;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.core.publisher.EventPublisher;
import org.atlas.platform.outbox.model.OutboxMessage;
import org.atlas.platform.outbox.processor.OutboxMessageProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMessageProcessorImpl implements OutboxMessageProcessor {

  private final EventPublisher eventPublisher;

  @Override
  public void process(OutboxMessage message) throws Exception {
    DomainEvent event = JsonUtil.toObject(message.getPayload(), DomainEvent.class);
    eventPublisher.publish(event);
  }
}
