package org.atlas.infrastructure.scheduler.quartz.core;

import java.util.Properties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

  private final DataSource dataSource;

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean() {
    SchedulerFactoryBean factory = new SchedulerFactoryBean();
    factory.setDataSource(dataSource);
    factory.setQuartzProperties(quartzProperties());
    factory.setOverwriteExistingJobs(true); // optional
    factory.setWaitForJobsToCompleteOnShutdown(true); // optional
    return factory;
  }

  private Properties quartzProperties() {
    Properties props = new Properties();
    props.setProperty("org.quartz.scheduler.instanceName", "DefaultScheduler");
    props.setProperty("org.quartz.scheduler.instanceId", "AUTO");

    // ThreadPool settings
    props.setProperty("org.quartz.threadPool.threadCount", "10");

    // JobStore settings
    props.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
    props.setProperty("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
    props.setProperty("org.quartz.jobStore.useProperties", "false");
    props.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_");
    props.setProperty("org.quartz.jobStore.isClustered", "true");

    return props;
  }
}
