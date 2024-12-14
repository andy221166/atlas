package org.atlas.platform.notification.email.ses.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sesv2.SesV2Client;

@Configuration
@Slf4j
public class SesConfig {

    @Bean
    public SesV2Client sesV2Client() {
        SesV2Client snsClient = SesV2Client.builder()
            .build();
        log.info("Initialized SES client");
        return snsClient;
    }
}
