package org.atlas.platform.rest.client.feign.client;

import org.atlas.platform.rest.client.contract.product.ListProductResponse;
import org.atlas.platform.rest.client.feign.core.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
    name = "product-service",
    url = "${app.rest.product.base-url:http://localhost:8082}",
    configuration = FeignConfig.class
)
public interface ProductFeignClient {

    @GetMapping("/api/products")
    ListProductResponse listProduct(@RequestParam List<Integer> ids);
}
