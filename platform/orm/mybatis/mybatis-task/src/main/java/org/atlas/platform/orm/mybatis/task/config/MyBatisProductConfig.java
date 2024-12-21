package org.atlas.platform.orm.mybatis.task.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(
    basePackages = "org.atlas.platform.orm.mybatis.task.mapper.product",
    sqlSessionFactoryRef = "productSqlSessionFactory"
)
public class MyBatisProductConfig {

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
  public SqlSessionFactory productSqlSessionFactory(
      @Qualifier("productDataSource") DataSource dataSource) throws Exception {
    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);

    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    sessionFactory.setMapperLocations(resolver.getResources("classpath*:mybatis/mapper/product/*.xml"));

    return sessionFactory.getObject();
  }

  @Bean
  public SqlSessionTemplate productSqlSessionTemplate(
      @Qualifier("productSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }
}

