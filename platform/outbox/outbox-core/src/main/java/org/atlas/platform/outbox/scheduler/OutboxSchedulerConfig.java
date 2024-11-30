package org.atlas.platform.outbox.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ConditionalOnProperty(name = "app.outbox.strategy", havingValue = "scheduler", matchIfMissing = true)
@EnableScheduling
public class OutboxSchedulerConfig {
}
