package org.atlas.platform.event.sns;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.event.sns")
@Data
public class SnsEventProps {

  private EventEndpoints snsTopicArn;
  private EventEndpoints sqsQueueUrl;
  private Integer batchSize;
  private Integer waitTimeSeconds;
  private Integer visibilityTimeout;

  @Data
  public static class EventEndpoints {

    private String userEvents;
    private String productEvents;
    private String orderEvents;
  }
}
