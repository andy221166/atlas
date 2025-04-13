package org.atlas.framework.messaging.outbox;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.json.JsonUtil;
import org.atlas.framework.messaging.MessagePublisher;
import org.atlas.framework.transaction.TransactionPort;

@RequiredArgsConstructor
@Slf4j
public class RelayOutboxMessageTask {

  private static final ExecutorService executor = Executors.newCachedThreadPool();
  private static final int MAX_RETRIES = 3;

  private final OutboxMessageRepository outboxMessageRepository;
  private final MessagePublisher messagePublisher;
  private final TransactionPort transactionPort;

  public void execute() {
    transactionPort.execute(() -> {
      // Find pending outbox messages
      List<OutboxMessageEntity> outboxMessages = outboxMessageRepository.findByStatusOrderByCreatedAt(
          OutboxMessageStatus.PENDING);
      if (outboxMessages.isEmpty()) {
        return;
      }

      // Process each outbox message in parallel
      List<CompletableFuture<Void>> futures = outboxMessages.stream()
          .map(outboxMessage -> CompletableFuture.runAsync(
              () -> processOutboxMessage(outboxMessage), executor))
          .toList();

      // Wait for all parallel tasks to complete
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    });
  }

  public void processOutboxMessage(OutboxMessageEntity outboxMessage) {
    try {
      // Parse message payload from JSON string to Java Object
      Class<?> messageClass = Class.forName(outboxMessage.getMessageClass());
      Object messagePayload = JsonUtil.getInstance()
          .toObject(outboxMessage.getMessagePayload(), messageClass);
      messagePublisher.publish(messagePayload, outboxMessage.getDestination(),
          outboxMessage.getMessageKey());
      outboxMessage.markAsProcessed();
    } catch (Exception e) {
      log.error("Failed to process outbox message {}", outboxMessage, e);
      outboxMessage.setError(e.getMessage());

      // Retry mechanism
      if (outboxMessage.getRetries() >= MAX_RETRIES) {
        outboxMessage.setStatus(OutboxMessageStatus.FAILED);
      } else {
        outboxMessage.incRetries();
      }
    }
    outboxMessageRepository.update(outboxMessage);
  }
}
