package org.atlas.platform.event.publisher.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.event.publisher.kafka")
@Data
public class KafkaEventPublisherProps {

  private Topics topics;

  @Data
  public static class Topics {

    private String userEvents;
    private String productEvents;
    private String orderEvents;
  }
}
