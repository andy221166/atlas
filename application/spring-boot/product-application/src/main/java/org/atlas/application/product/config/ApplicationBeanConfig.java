package org.atlas.application.product.config;

import org.atlas.framework.event.handler.EventHandler;
import org.atlas.framework.usecase.UseCaseHandler;
import org.atlas.infrastructure.config.core.ApplicationBeanRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfig {

  @Bean
  public ApplicationBeanRegistrar useCaseHandlerRegistrar() {
    return new ApplicationBeanRegistrar(
        UseCaseHandler.class,"org.atlas.domain.product.usecase");
  }

  @Bean
  public ApplicationBeanRegistrar eventHandlerRegistrar() {
    return new ApplicationBeanRegistrar(
        EventHandler.class,"org.atlas.domain.product.event");
  }
}
