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
    basePackages = "org.atlas.platform.orm.jpa.task.repository.user",
    entityManagerFactoryRef = "userEntityManagerFactory",
    transactionManagerRef = "userTransactionManager"
)
public class JpaUserConfig {

  @Bean
  @ConfigurationProperties("spring.datasource.user")
  public DataSourceProperties userDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource userDataSource() {
    return userDataSourceProperties()
        .initializeDataSourceBuilder()
        .build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(
      EntityManagerFactoryBuilder builder,
      @Qualifier("userDataSource") DataSource dataSource
  ) {
    return builder
        .dataSource(dataSource)
        .packages("org.atlas.platform.orm.jpa.task.entity.user")
        .persistenceUnit("user")
        .build();
  }

  @Bean
  public PlatformTransactionManager userTransactionManager(
      @Qualifier("userEntityManagerFactory") EntityManagerFactory entityManagerFactory
  ) {
    return new JpaTransactionManager(entityManagerFactory);
  }

  @Bean
  public TransactionTemplate userTransactionTemplate(
      @Qualifier("userTransactionManager") PlatformTransactionManager transactionManager
  ) {
    return new TransactionTemplate(transactionManager);
  }
}
