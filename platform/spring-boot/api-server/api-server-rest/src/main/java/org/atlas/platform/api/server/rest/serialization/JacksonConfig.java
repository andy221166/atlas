package org.atlas.platform.api.server.rest.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.atlas.platform.json.jackson.JacksonAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  @Bean
  public ObjectMapper objectMapper() {
    return JacksonAdapter.objectMapper;
  }
}
