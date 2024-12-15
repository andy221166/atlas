package org.atlas.platform.storage.s3.config;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Slf4j
public class S3Config {

  private S3Client s3Client;

  @Bean
  public S3Client s3Client() {
    this.s3Client = S3Client.builder()
        .build();
    log.info("Initialized S3 client");
    return this.s3Client;
  }

  @PreDestroy
  public void closeS3Client() {
    if (this.s3Client != null) {
      this.s3Client.close();
      log.info("Closed S3 client");
    }
  }
}
