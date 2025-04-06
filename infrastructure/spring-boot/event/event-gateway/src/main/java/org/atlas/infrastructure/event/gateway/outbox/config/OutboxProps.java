package org.atlas.infrastructure.event.gateway.outbox.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.event.outbox")
@Data
public class OutboxProps {

  private int maxRetries = 3;
}
