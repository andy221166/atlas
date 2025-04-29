package org.atlas.infrastructure.api.server.rest.core.config;

import lombok.RequiredArgsConstructor;
import org.atlas.framework.config.Application;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.infrastructure.api.server.rest.core.converter.StringToFileTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class RestWebConfig implements WebMvcConfigurer {

  private final ApplicationConfigPort applicationConfigPort;
  private final StringToFileTypeConverter stringToFileTypeConverter;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // Allow call APIs from API docs at gateway
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
            applicationConfigPort.getConfigAsInteger(Application.SYSTEM, "cors.max-age"));
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(stringToFileTypeConverter);
  }

  /**
   * Set the default media type for the responses
   */
  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.defaultContentType(MediaType.APPLICATION_JSON);
  }
}
