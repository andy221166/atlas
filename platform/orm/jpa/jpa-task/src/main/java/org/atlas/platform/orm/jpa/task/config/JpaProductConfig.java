package org.atlas.platform.orm.jpa.task.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EnableJpaRepositories(
    basePackages = "org.atlas.platform.orm.jpa.task.repository.product",
    entityManagerFactoryRef = "productEntityManagerFactory",
    transactionManagerRef = "productTransactionManager"
)
public class JpaProductConfig {

  @Bean
  @ConfigurationProperties("spring.datasource.product")
  public DataSourceProperties productDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource productDataSource() {
    return productDataSourceProperties()
        .initializeDataSourceBuilder()
        .build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean productEntityManagerFactory(
      EntityManagerFactoryBuilder builder,
      @Qualifier("productDataSource") DataSource dataSource) {
    return builder
        .dataSource(dataSource)
        .packages("org.atlas.platform.orm.jpa.task.entity.product")
        .persistenceUnit("product")
        .build();
  }

  @Bean
  public PlatformTransactionManager productTransactionManager(
      @Qualifier("productEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }

  @Bean
  public TransactionTemplate productTransactionTemplate(
      @Qualifier("productTransactionManager") PlatformTransactionManager transactionManager
  ) {
    return new TransactionTemplate(transactionManager);
  }
}
