package org.atlas.platform.event.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.event.kafka")
@Data
public class KafkaEventProps {

  private Topic topic;

  @Data
  public static class Topic {

    private String userEvents;
    private String productEvents;
    private String orderEvents;
  }
}
