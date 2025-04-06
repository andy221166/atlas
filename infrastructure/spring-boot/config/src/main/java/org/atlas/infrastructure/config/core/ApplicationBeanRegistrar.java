package org.atlas.infrastructure.config.core;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

/**
 * A BeanFactoryPostProcessor may interact with and modify bean definitions, but never bean instances.
 * Allows for custom modification of an application context’s bean definitions, adapting the bean property values of the context’s underlying bean factory.
 * Application contexts can auto-detect BeanFactoryPostProcessor beans in their bean definitions and apply them before any other beans get created.
 */
@RequiredArgsConstructor
@Slf4j
public class ApplicationBeanRegistrar implements BeanFactoryPostProcessor {

  private final Class<?> beanClass;
  private final String basePackage;

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    registerBeans(beanFactory);
  }

  private void registerBeans(ConfigurableListableBeanFactory beanFactory) {
    BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
    Set<BeanDefinition> candidates = scanBeanCandidates();
    log.info("Starting register application beans from {} candidates", candidates.size());
    for (BeanDefinition candidate : candidates) {
      registerBean(candidate, registry);
    }
    log.info("Finished register application beans");
  }

  private Set<BeanDefinition> scanBeanCandidates() {
    ClassPathScanningCandidateComponentProvider scanner =
        new ClassPathScanningCandidateComponentProvider(false);

    scanner.addIncludeFilter(new AssignableTypeFilter(beanClass));
    return scanner.findCandidateComponents(basePackage);
  }

  private void registerBean(BeanDefinition candidate, BeanDefinitionRegistry registry) {
    String className = candidate.getBeanClassName();
    try {
      Class<?> clazz = Class.forName(className);
      String beanName = StringUtils.uncapitalize(clazz.getSimpleName());

      if (registry.containsBeanDefinition(beanName)) {
        throw new IllegalStateException(
            "Duplicate bean name '" + beanName + "' for " + clazz.getName());
      }

      BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
      registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Failed to load class: " + className, e);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to register use case: " + className, e);
    }
  }
}
