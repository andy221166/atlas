package org.atlas.service.catalog.api.client.rest.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.atlas.service.catalog.api.client.rest.feign")
public class CatalogFeignClientConfig {
}
