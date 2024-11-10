package org.atlas.platform.event.core.outbox;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ConditionalOnProperty(name = "app.outbox.enabled", havingValue = "true")
@EntityScan(basePackages = "org.atlas.platform.event.core.outbox")
@EnableJpaRepositories(basePackages = "org.atlas.platform.event.core.outbox")
@EnableScheduling
public class OutboxConfig {
}
