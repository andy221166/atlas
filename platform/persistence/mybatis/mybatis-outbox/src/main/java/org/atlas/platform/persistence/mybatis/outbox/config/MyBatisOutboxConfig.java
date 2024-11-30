package org.atlas.platform.persistence.mybatis.outbox.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "org.atlas.platform.persistence.mybatis.outbox")
public class MyBatisOutboxConfig {
}
