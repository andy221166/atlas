package org.atlas.platform.api.client.rest.feign.core;

import java.util.List;
import org.atlas.platform.rest.client.contract.product.ListProductBulkResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "product-service",
    url = "${app.rest-client.product-service.base-url:http://localhost:8082}",
    configuration = FeignConfig.class
)
public interface ProductFeignClient {

  @GetMapping("/api/products/bulk")
  ListProductBulkResponse listProductBulk(@RequestParam("ids") List<Integer> ids);
}
