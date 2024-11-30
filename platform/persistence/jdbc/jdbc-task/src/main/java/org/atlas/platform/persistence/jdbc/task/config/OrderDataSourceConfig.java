package org.atlas.platform.persistence.jdbc.task.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class OrderDataSourceConfig {

  @Bean
  @ConfigurationProperties("spring.datasource.order")
  public DataSourceProperties orderDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource orderDataSource() {
    return orderDataSourceProperties()
        .initializeDataSourceBuilder()
        .build();
  }

  @Bean
  public JdbcTemplate orderJdbcTemplate(@Qualifier("orderDataSource") DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  public NamedParameterJdbcTemplate orderNamedParameterJdbcTemplate(
      @Qualifier("orderDataSource") DataSource dataSource) {
    return new NamedParameterJdbcTemplate(dataSource);
  }

  @Bean
  public DataSourceTransactionManager orderTransactionManager(
      @Qualifier("orderDataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }
}
