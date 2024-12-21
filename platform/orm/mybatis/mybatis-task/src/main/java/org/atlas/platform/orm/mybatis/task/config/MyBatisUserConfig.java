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
    basePackages = "org.atlas.platform.orm.mybatis.task.mapper.user",
    sqlSessionFactoryRef = "userSqlSessionFactory"
)
public class MyBatisUserConfig {

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
  public SqlSessionFactory userSqlSessionFactory(
      @Qualifier("userDataSource") DataSource dataSource) throws Exception {
    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);

    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    sessionFactory.setMapperLocations(resolver.getResources("classpath*:mybatis/mapper/user/*.xml"));

    return sessionFactory.getObject();
  }

  @Bean
  public SqlSessionTemplate userSqlSessionTemplate(
      @Qualifier("userSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }
}

