package org.atlas.platform.orm.jdbc.aggregator.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class ProductDataSourceConfig {

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
  public NamedParameterJdbcTemplate productNamedParameterJdbcTemplate(
      @Qualifier("productDataSource") DataSource dataSource) {
    return new NamedParameterJdbcTemplate(dataSource);
  }

  @Bean
  public DataSourceTransactionManager productTransactionManager(
      @Qualifier("productDataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }
}
