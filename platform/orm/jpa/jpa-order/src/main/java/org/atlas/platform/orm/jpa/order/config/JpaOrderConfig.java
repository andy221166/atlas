package org.atlas.platform.orm.jpa.order.config;

import org.atlas.platform.orm.jpa.core.repository.JpaBaseRepositoryImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Need to configure base packages of JPA entities and repositories in case of multi-modules
 * project.
 */
@Configuration
@EntityScan(basePackages = "org.atlas.platform.orm.jpa.order.entity")
@EnableJpaRepositories(
    basePackages = "org.atlas.platform.orm.jpa.order.repository",
    repositoryBaseClass = JpaBaseRepositoryImpl.class
)
public class JpaOrderConfig {

}
