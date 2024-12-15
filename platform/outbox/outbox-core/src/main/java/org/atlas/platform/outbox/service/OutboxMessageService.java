package org.atlas.platform.outbox.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.outbox.model.OutboxMessage;
import org.atlas.platform.outbox.model.OutboxMessageStatus;
import org.atlas.platform.outbox.processor.OutboxMessageProcessor;
import org.atlas.platform.outbox.repository.OutboxMessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxMessageService {

  @Value("${app.outbox.max-retries:3}")
  private int maxRetries;

  private final OutboxMessageRepository repository;
  private final OutboxMessageProcessor processor;

  public void processOutboxMessage(OutboxMessage outboxMessage) {
    try {
      processor.process(outboxMessage);

      outboxMessage.toBeProcessed();
      repository.update(outboxMessage);
      log.info("Processed outbox message: {}", outboxMessage);
    } catch (Exception e) {
      log.error("Failed to process outbox message", e);
      outboxMessage.setError(e.getMessage());
      if (outboxMessage.getRetries() == maxRetries) {
        outboxMessage.setStatus(OutboxMessageStatus.FAILED);
        repository.update(outboxMessage);
      } else {
        outboxMessage.incRetries();
        repository.update(outboxMessage);
      }
    }
  }
}
