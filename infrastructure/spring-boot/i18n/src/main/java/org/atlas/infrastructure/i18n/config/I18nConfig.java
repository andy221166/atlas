package org.atlas.infrastructure.i18n.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.config.ApplicationConfigPort;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class I18nConfig {

  private final ApplicationConfigPort applicationConfigPort;

  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    // src/main/resources/messages_{locale}.properties file
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setDefaultLocale(applicationConfigPort.getLocale());
    return messageSource;
  }
}
