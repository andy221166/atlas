package org.atlas.infrastructure.event.gateway.outbox.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.event.DomainEvent;
import org.atlas.infrastructure.event.gateway.EventPublisher;
import org.atlas.infrastructure.event.gateway.outbox.entity.OutboxMessage;
import org.atlas.infrastructure.event.gateway.outbox.entity.OutboxMessageStatus;
import org.atlas.infrastructure.event.gateway.outbox.repository.OutboxMessageRepository;
import org.atlas.infrastructure.json.JsonUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RelayOutboxMessageService {

  private static final int MAX_RETRIES = 3;

  private final OutboxMessageRepository outboxMessageRepository;
  private final EventPublisher eventPublisher;
  private final TaskExecutor outboxMessageExecutor;

  public RelayOutboxMessageService(
      OutboxMessageRepository outboxMessageRepository,
      EventPublisher eventPublisher,
      @Qualifier("outboxMessageExecutor") TaskExecutor outboxMessageExecutor) {
    this.outboxMessageRepository = outboxMessageRepository;
    this.eventPublisher = eventPublisher;
    this.outboxMessageExecutor = outboxMessageExecutor;
  }

  @Transactional
  public void processPendingOutboxMessages() {
    // Find pending outbox messages
    List<OutboxMessage> outboxMessages = outboxMessageRepository.findByStatusOrderByCreatedAt(
        OutboxMessageStatus.PENDING);
    if (outboxMessages.isEmpty()) {
      return;
    }

    // Process each outbox message in parallel
    List<CompletableFuture<Void>> futures = outboxMessages.stream()
        .map(outboxMessage ->
            CompletableFuture.runAsync(() -> processOutboxMessage(outboxMessage),
                outboxMessageExecutor))
        .toList();

    // Wait for all parallel tasks to complete
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW) // Each message has its own transaction
  public void processOutboxMessage(OutboxMessage outboxMessage) {
    try {
      // Parse event JSON string to object
      DomainEvent event = JsonUtil.getInstance()
          .toObject(outboxMessage.getEventJson(), outboxMessage.getEventType().getEventClass());
      eventPublisher.publish(event, outboxMessage.getDestination());
      outboxMessage.markAsProcessed();
    } catch (Exception e) {
      log.error("Failed to process outbox message {} of event {} {}",
          outboxMessage.getId(), outboxMessage.getEventType(), outboxMessage.getEventJson(), e);
      outboxMessage.setError(e.getMessage());

      // Retry mechanism
      if (outboxMessage.getRetries() >= MAX_RETRIES) {
        outboxMessage.setStatus(OutboxMessageStatus.FAILED);
      } else {
        outboxMessage.incRetries();
      }
    }
    outboxMessageRepository.save(outboxMessage);
  }
}
