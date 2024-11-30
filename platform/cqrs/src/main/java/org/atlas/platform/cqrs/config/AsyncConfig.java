package org.atlas.platform.cqrs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

  /**
   * Configures and returns a TaskExecutor bean for asynchronous command execution.
   *
   * @return a configured ThreadPoolTaskExecutor for async command execution tasks
   */
  @Bean
  public TaskExecutor asyncCommandExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(0); // No core threads
    executor.setMaxPoolSize(Integer.MAX_VALUE); // Maximum pool size
    executor.setQueueCapacity(0); // Use a SynchronousQueue-like behavior
    executor.setKeepAliveSeconds(60); // Threads will be terminated after 60 seconds of inactivity
    executor.setAllowCoreThreadTimeOut(true); // Allow core threads to time out
    executor.setThreadNamePrefix("AsyncCommandExecutor-");
    executor.initialize();
    return executor;
  }
}
