package org.atlas.infrastructure.scheduler.quartz.adapter.user.job;

import lombok.RequiredArgsConstructor;
import org.atlas.framework.task.outbox.RelayOutboxMessageTask;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
@RequiredArgsConstructor
public class RelayOutboxMessageJob implements Job {

  private final RelayOutboxMessageTask task;

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    try {
      task.execute();
    } catch (Exception e) {
      throw new JobExecutionException(e);
    }
  }
}
