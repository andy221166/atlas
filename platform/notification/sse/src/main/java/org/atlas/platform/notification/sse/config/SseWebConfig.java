package org.atlas.platform.notification.sse.config;

import org.atlas.commons.constant.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SseWebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(Constant.ALLOWED_HOSTS)
        .allowedMethods("*")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}
