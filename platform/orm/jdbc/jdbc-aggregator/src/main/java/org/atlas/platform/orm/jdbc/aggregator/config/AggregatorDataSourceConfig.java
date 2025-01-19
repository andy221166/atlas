package org.atlas.platform.orm.jdbc.aggregator.config;

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
public class AggregatorDataSourceConfig {

  @Bean
  @ConfigurationProperties("spring.datasource.aggregator")
  public DataSourceProperties aggregatorDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource aggregatorDataSource() {
    return aggregatorDataSourceProperties()
        .initializeDataSourceBuilder()
        .build();
  }

  @Bean
  public JdbcTemplate aggregatorJdbcTemplate(@Qualifier("aggregatorDataSource") DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  public NamedParameterJdbcTemplate aggregatorNamedParameterJdbcTemplate(
      @Qualifier("aggregatorDataSource") DataSource dataSource) {
    return new NamedParameterJdbcTemplate(dataSource);
  }

  @Bean
  public DataSourceTransactionManager aggregatorTransactionManager(
      @Qualifier("aggregatorDataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }
}
