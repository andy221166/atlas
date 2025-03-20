package org.atlas.platform.event.sns;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
@Slf4j
public class SnsConfig {

  private SnsClient snsClient;

  @Bean
  public SnsClient snsClient() {
    this.snsClient = SnsClient.builder()
        .build();
    log.info("Initialized SNS client");
    return this.snsClient;
  }

  @PreDestroy
  public void closeSnsClient() {
    if (this.snsClient != null) {
      this.snsClient.close();
      log.info("Closed SNS client");
    }
  }
}
