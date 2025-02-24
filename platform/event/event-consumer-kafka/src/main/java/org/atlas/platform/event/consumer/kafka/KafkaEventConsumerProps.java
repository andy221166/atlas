package org.atlas.platform.event.consumer.kafka;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.event.consumer.kafka")
@Data
public class KafkaEventConsumerProps {

  private List<String> topics;
}
