package org.atlas.platform.event.gateway.outbox.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.gateway.outbox.service.OutboxMessageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RelayOutboxMessageTask {

  private final OutboxMessageService outboxMessageService;

  /**
   * Run every minute
   */
  @Scheduled(cron = "*/15 * * * * *")
  public void run() {
    outboxMessageService.processPendingOutboxMessages();
  }
}
