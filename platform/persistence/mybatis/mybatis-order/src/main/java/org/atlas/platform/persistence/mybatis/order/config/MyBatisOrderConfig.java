package org.atlas.platform.persistence.mybatis.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "org.atlas.platform.persistence.mybatis.order")
public class MyBatisOrderConfig {
}
