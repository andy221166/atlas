package org.atlas.platform.persistence.mybatis.report.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "org.atlas.platform.persistence.mybatis.report")
public class MyBatisReportConfig {
}
