package org.atlas.platform.orm.mybatis.outbox.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "org.atlas.platform.orm.mybatis.outbox")
public class MyBatisOutboxConfig {
}
