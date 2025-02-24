package org.atlas.platform.event.core.outbox.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.cqrs.event.outbox")
@Data
public class OutboxProps {

  private int maxRetries = 3;
}
