package org.atlas.platform.api.client.product.rest.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.atlas.service.product.api.client.rest.feign")
public class ProductFeignClientConfig {

}
