package org.atlas.service.notification.adapter.sse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration("sseWebConfig")
public class SseWebConfig implements WebMvcConfigurer {

  // TODO: Consider to put it into config-server
  private static final String[] ALLOWED_HOSTS = {
      "http://localhost:9000" // Frontend
  };

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(ALLOWED_HOSTS)
        .allowedMethods("*")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600L);
  }
}
