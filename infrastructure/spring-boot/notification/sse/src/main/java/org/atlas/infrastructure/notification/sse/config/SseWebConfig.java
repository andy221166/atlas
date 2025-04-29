package org.atlas.infrastructure.notification.sse.config;

import lombok.RequiredArgsConstructor;
import org.atlas.framework.config.Application;
import org.atlas.framework.config.ApplicationConfigPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration("sseWebConfig")
@RequiredArgsConstructor
public class SseWebConfig implements WebMvcConfigurer {

  private final ApplicationConfigPort applicationConfigPort;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(
            applicationConfigPort.getConfigAsList(Application.SYSTEM, "cors.allowed-origins")
                .toArray(new String[0]))
        .allowedMethods(
            applicationConfigPort.getConfigAsList(Application.SYSTEM, "cors.allowed-methods")
                .toArray(new String[0]))
        .allowedHeaders(
            applicationConfigPort.getConfigAsList(Application.SYSTEM, "cors.allowed-headers")
                .toArray(new String[0]))
        .allowCredentials(
            applicationConfigPort.getConfigAsBoolean(Application.SYSTEM, "cors.allow-credentials"))
        .maxAge(
            applicationConfigPort.getConfigAsLong(Application.SYSTEM, "cors.max-age"));
  }
}
