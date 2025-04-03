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

    private String userRegisteredEvent;
    private String productCreatedEvent;
    private String productUpdatedEvent;
    private String productDeletedEvent;
    private String orderCreatedEvent;
    private String reserveQuantitySucceededEvent;
    private String reserveQuantityFailedEvent;
    private String orderConfirmedEvent;
    private String orderCanceledEvent;
  }
}
