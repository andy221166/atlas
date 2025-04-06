package org.atlas.infrastructure.i18n.config;

import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
@Slf4j
public class LocaleConfig {

  @Value("${app.locale:en-US}")
  private String appLocale;

  @Bean
  public Locale locale() {
    log.info("Application locale: {}", appLocale);
    return Locale.forLanguageTag(appLocale);
  }

  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    // src/main/resources/messages_{locale}.properties file
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setDefaultLocale(locale());
    return messageSource;
  }
}
