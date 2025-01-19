package org.atlas.platform.rest.server.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.atlas.commons.util.json.JacksonOps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  @Bean
  public ObjectMapper objectMapper() {
    return JacksonOps.objectMapper;
  }
}
