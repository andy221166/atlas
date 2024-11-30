package org.atlas.platform.taskscheduler.quartz.job;

import lombok.RequiredArgsConstructor;
import org.atlas.service.task.contract.core.CancelOverdueProcessingOrdersTask;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CancelOverdueProcessingOrdersJob implements Job {

  private final CancelOverdueProcessingOrdersTask task;

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    task.execute();
  }
}
