package org.atlas.platform.notification.email.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.email")
@Data
public class EmailProps {

  private String source;
}
