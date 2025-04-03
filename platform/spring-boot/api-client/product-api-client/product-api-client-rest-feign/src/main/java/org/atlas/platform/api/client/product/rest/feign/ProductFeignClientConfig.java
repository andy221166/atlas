package org.atlas.platform.api.client.product.rest.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.atlas.platform.api.client.product.rest.feign")
public class ProductFeignClientConfig {

}
