package org.atlas.edge.gateway.ratelimiter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.data.redis")
@Data
public class RedisConfigProperties {

  private String host;
  private int port;
  private String password;
}
