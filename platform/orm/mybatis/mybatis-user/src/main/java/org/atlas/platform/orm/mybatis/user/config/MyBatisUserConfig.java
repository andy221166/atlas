package org.atlas.platform.orm.mybatis.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "org.atlas.platform.persistence.mybatis.user")
public class MyBatisUserConfig {
}
