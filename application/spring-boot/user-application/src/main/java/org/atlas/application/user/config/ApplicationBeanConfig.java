package org.atlas.application.user.config;

import org.atlas.framework.usecase.UseCaseHandler;
import org.atlas.infrastructure.config.core.ApplicationBeanRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfig {

  @Bean
  public ApplicationBeanRegistrar useCaseRegistrar() {
    return new ApplicationBeanRegistrar(UseCaseHandler.class, "org.atlas.domain.user.usecase");
  }
}
