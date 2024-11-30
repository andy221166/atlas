package org.atlas.platform.persistence.jpa.task.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EnableJpaRepositories(
    basePackages = "org.atlas.platform.persistence.jpa.task.repository.order",
    entityManagerFactoryRef = "orderEntityManagerFactory",
    transactionManagerRef = "orderTransactionManager"
)
public class JpaOrderConfig {

  @Bean
  @Primary
  @ConfigurationProperties("spring.datasource.order")
  public DataSourceProperties orderDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @Primary
  public DataSource orderDataSource() {
    return orderDataSourceProperties()
        .initializeDataSourceBuilder()
        .build();
  }

  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean orderEntityManagerFactory(
      EntityManagerFactoryBuilder builder,
      @Qualifier("orderDataSource") DataSource dataSource
  ) {
    return builder
        .dataSource(dataSource)
        .packages("org.atlas.platform.persistence.jpa.task.entity.order")
        .persistenceUnit("order")
        .build();
  }

  @Bean
  @Primary
  public PlatformTransactionManager orderTransactionManager(
      @Qualifier("orderEntityManagerFactory") EntityManagerFactory entityManagerFactory
  ) {
    return new JpaTransactionManager(entityManagerFactory);
  }

  @Bean
  @Primary
  public TransactionTemplate orderTransactionTemplate(
      @Qualifier("orderTransactionManager") PlatformTransactionManager transactionManager
  ) {
    return new TransactionTemplate(transactionManager);
  }
}
