package org.atlas.infrastructure.scheduler.spring.task;

import lombok.RequiredArgsConstructor;
import org.atlas.framework.task.RelayOutboxMessageTask;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RelayOutboxMessageTaskRunner {

  private final RelayOutboxMessageTask relayOutboxMessageTask;

  /**
   * Run every minute
   */
  @Scheduled(cron = "0 */1 * * * *")
  public void run() throws Exception {
    relayOutboxMessageTask.execute();
  }
}
