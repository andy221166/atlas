package org.atlas.service.user.adapter.persistence.mybatis.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "org.atlas.service.user.adapter.outbound.persistence.mybatis.mapper")
public class UserMyBatisConfig {
}
