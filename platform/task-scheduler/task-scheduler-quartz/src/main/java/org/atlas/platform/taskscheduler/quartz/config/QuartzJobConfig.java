package org.atlas.platform.taskscheduler.quartz.config;

import org.atlas.platform.taskscheduler.quartz.job.CancelOverdueProcessingOrdersJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzJobConfig {

  @Bean
  public JobDetail reportOrderJobDetail() {
    return JobBuilder.newJob()
        .ofType(CancelOverdueProcessingOrdersJob.class)
        .withIdentity(CancelOverdueProcessingOrdersJob.class.getSimpleName())
        .storeDurably()
        .build();
  }

  @Bean
  public Trigger reportOrderTrigger(JobDetail reportOrderJobDetail) {
    return TriggerBuilder.newTrigger()
        .forJob(reportOrderJobDetail)
        .withIdentity(CancelOverdueProcessingOrdersJob.class.getSimpleName())
        // Run every 15 minutes
        .withSchedule(CronScheduleBuilder.cronSchedule("0 */15 * * * ?"))
        .build();
  }
}
