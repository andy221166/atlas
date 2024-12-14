package org.atlas.platform.event.sns.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.event.sns")
@Data
public class SnsEventProps {

  private String orderTopicArn;
  private String orderQueueUrl;
  private Integer batchSize;
  private Integer waitTimeSeconds;
  private Integer visibilityTimeout;
}
