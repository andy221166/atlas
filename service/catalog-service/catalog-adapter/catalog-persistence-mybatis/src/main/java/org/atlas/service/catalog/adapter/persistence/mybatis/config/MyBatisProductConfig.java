package org.atlas.service.catalog.adapter.persistence.mybatis.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "org.atlas.platform.orm.mybatis.product")
public class MyBatisProductConfig {
}
