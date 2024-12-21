package org.atlas.platform.rest.client.feign.client;

import java.util.List;
import org.atlas.platform.rest.client.contract.product.ListProductResponse;
import org.atlas.platform.rest.client.feign.core.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "product-service",
    url = "${app.rest-client.product.base-url:http://localhost:8082}",
    configuration = FeignConfig.class
)
public interface ProductFeignClient {

  @GetMapping("/api/products")
  ListProductResponse listProduct(@RequestParam("ids") List<Integer> ids);
}
