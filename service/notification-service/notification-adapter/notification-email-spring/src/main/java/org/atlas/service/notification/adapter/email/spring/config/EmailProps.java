package org.atlas.service.notification.adapter.email.spring.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.email")
@Data
public class EmailProps {

  private String sender;
}
