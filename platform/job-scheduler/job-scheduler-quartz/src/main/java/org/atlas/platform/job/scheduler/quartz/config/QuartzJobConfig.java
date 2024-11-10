package org.atlas.platform.job.scheduler.quartz.config;

import org.atlas.platform.job.scheduler.quartz.job.AggregateReportOrderJob;
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
            .ofType(AggregateReportOrderJob.class)
            .withIdentity(AggregateReportOrderJob.class.getSimpleName())
            .storeDurably()
            .build();
    }

    @Bean
    public Trigger reportOrderTrigger(JobDetail reportOrderJobDetail) {
        return TriggerBuilder.newTrigger()
            .forJob(reportOrderJobDetail)
            .withIdentity(AggregateReportOrderJob.class.getSimpleName())
            // Run at midnight
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
            .build();
    }
}
