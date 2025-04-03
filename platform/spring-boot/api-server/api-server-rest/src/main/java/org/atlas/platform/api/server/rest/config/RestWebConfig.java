package org.atlas.platform.api.server.rest.config;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.converter.StringToFileTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class RestWebConfig implements WebMvcConfigurer {

  private final StringToFileTypeConverter stringToFileTypeConverter;

  // TODO: Consider to put it into config-server
  private static final String[] ALLOWED_ORIGINS = {
      "http://localhost:8080", // Gateway
      "http://localhost:9000" // Frontend
  };

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // Allow call APIs from API docs at gateway
    registry.addMapping("/**")
        .allowedOrigins(ALLOWED_ORIGINS)
        .allowedMethods("*")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);
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
