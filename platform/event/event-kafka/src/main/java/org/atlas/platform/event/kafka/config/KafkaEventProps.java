package org.atlas.platform.event.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.event.kafka")
@Data
public class KafkaEventProps {

  private String orderTopic;
}
