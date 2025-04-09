package org.atlas.infrastructure.scheduler.quartz.adapter.auth.config;

import org.atlas.infrastructure.scheduler.quartz.adapter.auth.job.RelayOutboxMessageJob;
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
  public JobDetail relayOutboxMessageJobDetail() {
    return JobBuilder.newJob()
        .ofType(RelayOutboxMessageJob.class)
        .withIdentity(RelayOutboxMessageJob.class.getSimpleName())
        .storeDurably()
        .build();
  }

  @Bean
  public Trigger relayOutboxMessageTrigger(JobDetail relayOutboxMessageJobDetail) {
    return TriggerBuilder.newTrigger()
        .forJob(relayOutboxMessageJobDetail)
        .withIdentity(RelayOutboxMessageJob.class.getSimpleName())
        // Run every minute
        .withSchedule(CronScheduleBuilder.cronSchedule("0 */1 * * * ?")
            .withMisfireHandlingInstructionFireAndProceed()) // Handle misfires
        .build();
  }
}
