package org.atlas.platform.outbox.scheduler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.lock.core.LockService;
import org.atlas.platform.outbox.model.OutboxMessage;
import org.atlas.platform.outbox.model.OutboxMessageStatus;
import org.atlas.platform.outbox.repository.OutboxMessageRepository;
import org.atlas.platform.outbox.service.OutboxMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@ConditionalOnProperty(name = "app.outbox.strategy", havingValue = "scheduler", matchIfMissing = true)
@RequiredArgsConstructor
@Slf4j
public class RelayOutboxMessageTask {

  private final OutboxMessageRepository outboxMessageRepository;
  private final OutboxMessageService outboxMessageService;
  private final LockService lockService;
  private final TransactionTemplate transactionTemplate;

  @Value("${spring.application.name}")
  private String applicationName;

  /**
   * Run every minute
   */
  @Scheduled(cron = "0 * * * * *")
  public void run() {
    if (!lockService.acquireLock(lockKey())) {
      return;
    }

    List<OutboxMessage> outboxMessages = outboxMessageRepository.findByStatus(
        OutboxMessageStatus.PENDING);
    log.info("Found {} pending outbox message(s)", outboxMessages.size());
    if (outboxMessages.isEmpty()) {
      return;
    }

    for (OutboxMessage message : outboxMessages) {
      transactionTemplate.executeWithoutResult(status -> {
        outboxMessageService.processOutboxMessage(message);
      });
    }
  }

  private String lockKey() {
    return String.format("event:%s", applicationName);
  }
}
