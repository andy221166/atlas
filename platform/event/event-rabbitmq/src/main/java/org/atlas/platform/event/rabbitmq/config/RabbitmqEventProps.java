package org.atlas.platform.event.rabbitmq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.event.rabbitmq")
@Data
public class RabbitmqEventProps {

  private String orderExchange;
  private String orderQueue;
}
