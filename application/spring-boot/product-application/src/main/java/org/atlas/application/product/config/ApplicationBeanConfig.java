package org.atlas.application.product.config;

import org.atlas.framework.domain.event.handler.EventHandler;
import org.atlas.framework.domain.service.DomainService;
import org.atlas.framework.usecase.handler.UseCaseHandler;
import org.atlas.infrastructure.bootstrap.ApplicationBeanRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfig {

  @Bean
  public static ApplicationBeanRegistrar useCaseHandlerRegistrar() {
    return new ApplicationBeanRegistrar(
        UseCaseHandler.class,"org.atlas.domain.product.usecase");
  }

  @Bean
  public static ApplicationBeanRegistrar eventHandlerRegistrar() {
    return new ApplicationBeanRegistrar(
        EventHandler.class,"org.atlas.domain.product.event");
  }

  @Bean
  public static ApplicationBeanRegistrar serviceRegistrar() {
    return new ApplicationBeanRegistrar(
        DomainService.class,"org.atlas.domain.product.service");
  }
}
