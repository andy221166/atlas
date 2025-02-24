package org.atlas.platform.api.client.rest.feign;

import feign.Logger;
import feign.Request;
import java.util.concurrent.TimeUnit;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.atlas.platform.api.client.rest.feign.core")
public class FeignConfig {

  @Bean
  public Logger.Level feignLoggerLevel() {
    return Logger.Level.HEADERS;
  }

  @Bean
  public Request.Options feignRequestOptions() {
    return new Request.Options(30, TimeUnit.SECONDS, 30, TimeUnit.SECONDS, true);
  }
}
