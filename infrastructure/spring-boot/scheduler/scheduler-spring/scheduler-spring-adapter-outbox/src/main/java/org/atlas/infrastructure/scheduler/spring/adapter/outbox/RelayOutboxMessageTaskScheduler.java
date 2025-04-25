package org.atlas.infrastructure.scheduler.spring.adapter.outbox;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.atlas.framework.messaging.outbox.RelayOutboxMessageTask;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RelayOutboxMessageTaskScheduler {

  private final RelayOutboxMessageTask relayOutboxMessageTask;

  /**
   * Run every minute
   */
  @Scheduled(cron = "0 */1 * * * *")
  @SchedulerLock(
      name = "RelayOutboxMessageTask",
      // lock held for at least 50 seconds.
      // Even if the task finishes in 5 seconds, lock remains for 50s.
      // -> Prevents other app instances from running it again too soon.
      lockAtLeastFor = "PT50S",
      // lock auto-released after 2 minutes if crashed.
      // If the task crashes or hangs, Redis will auto-expire the lock after 2 minutes.
      // -> Prevents deadlocks.
      lockAtMostFor = "PT2M"
  )
  public void run() throws Exception {
    relayOutboxMessageTask.execute();
  }
}
