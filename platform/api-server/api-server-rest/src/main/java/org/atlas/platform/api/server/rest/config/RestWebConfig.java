package org.atlas.platform.api.server.rest.config;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.converter.StringToFileTypeConverter;
import org.atlas.platform.commons.constant.Constant;
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

  @Override
  public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(Constant.ALLOWED_HOSTS)
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowCredentials(true);
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
