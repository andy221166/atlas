package org.atlas.platform.orm.mybatis.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "org.atlas.platform.orm.mybatis.order")
public class MyBatisOrderConfig {
}
