package org.atlas.platform.event.gateway.outbox.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.gateway.EventPublisher;
import org.atlas.platform.event.gateway.outbox.config.OutboxProps;
import org.atlas.platform.event.gateway.outbox.model.OutboxMessage;
import org.atlas.platform.event.gateway.outbox.model.OutboxMessageStatus;
import org.atlas.platform.event.gateway.outbox.repository.OutboxMessageRepository;
import org.atlas.platform.json.JsonUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class OutboxMessageService {

  private final OutboxMessageRepository outboxMessageRepository;
  private final EventPublisher eventPublisher;
  private final OutboxProps outboxProps;
  private final TaskExecutor outboxMessageExecutor;

  public OutboxMessageService(
      OutboxMessageRepository outboxMessageRepository,
      EventPublisher eventPublisher,
      OutboxProps outboxProps,
      @Qualifier("outboxMessageExecutor") TaskExecutor outboxMessageExecutor) {
    this.outboxMessageRepository = outboxMessageRepository;
    this.eventPublisher = eventPublisher;
    this.outboxProps = outboxProps;
    this.outboxMessageExecutor = outboxMessageExecutor;
  }

  public void insertOutboxMessage(DomainEvent event, String destination) {
    OutboxMessage outboxMessage = new OutboxMessage();
    outboxMessage.setEvent(JsonUtil.getInstance().toJson(event));
    outboxMessage.setDestination(destination);
    outboxMessage.setStatus(OutboxMessageStatus.PENDING);
    outboxMessage.setRetries(0);
    outboxMessageRepository.insert(outboxMessage);
    log.info("Inserted outbox message {} of event {}",
        outboxMessage.getId(), outboxMessage.getEvent());
  }

  @Transactional
  public void processPendingOutboxMessages() {
    // Find and lock pending outbox messages
    List<OutboxMessage> outboxMessages = outboxMessageRepository.findByStatusWithLock(
        OutboxMessageStatus.PENDING);
    log.info("Found and locked {} pending outbox message(s)", outboxMessages.size());
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

    log.info("All outbox messages were processed, releasing lock");
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW) // Each message has its own transaction
  public void processOutboxMessage(OutboxMessage outboxMessage) {
    try {
      // Parse event JSON string to object
      String eventTypeName = (String) JsonUtil.getInstance()
          .getNodeValue(outboxMessage.getEvent(), "eventType");
      EventType eventType = EventType.valueOf(eventTypeName);
      DomainEvent event = JsonUtil.getInstance()
          .toObject(outboxMessage.getEvent(), eventType.getEventClass());

      eventPublisher.publish(event, outboxMessage.getDestination());

      outboxMessage.toBeProcessed();
      log.info("Processed outbox message {} of event {}",
          outboxMessage.getId(), outboxMessage.getEvent());
    } catch (Exception e) {
      log.error("Failed to process outbox message {} of event {}",
          outboxMessage.getId(), outboxMessage.getEvent(), e);
      outboxMessage.setError(e.getMessage());

      // Retry mechanism
      if (outboxMessage.getRetries() >= outboxProps.getMaxRetries()) {
        outboxMessage.setStatus(OutboxMessageStatus.FAILED);
      } else {
        outboxMessage.incRetries();
      }
    }
    outboxMessageRepository.save(outboxMessage);
  }
}
