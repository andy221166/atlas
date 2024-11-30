package org.atlas.platform.rest.server.core.config;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.rest.server.core.converter.StringToFileTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class RestWebConfig implements WebMvcConfigurer {

  private final StringToFileTypeConverter stringToFileTypeConverter;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//            .allowedOrigins(Constant.ALLOWED_HOSTS)
//            .allowedMethods("*")
//            .allowedHeaders("*")
//            .allowCredentials(true);
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(stringToFileTypeConverter);
  }
}
