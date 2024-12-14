package org.atlas.platform.event.sns.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
@Slf4j
public class SnsConfig {

    @Bean
    public SnsClient snsClient() {
        SnsClient snsClient = SnsClient.builder()
            .build();
        log.info("Initialized SNS client");
        return snsClient;
    }
}
