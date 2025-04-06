package org.atlas.infrastructure.internalapi.product.rest.feign;

import org.atlas.infrastructure.api.client.core.rest.feign.FeignConfig;
import org.atlas.infrastructure.api.client.core.rest.model.Response;
import org.atlas.infrastructure.internalapi.product.rest.model.ListProductRequest;
import org.atlas.infrastructure.internalapi.product.rest.model.ListProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "product-service",
    url = "${app.api-client.rest.product-service.base-url:http://localhost:8082}",
    configuration = FeignConfig.class
)
public interface ProductFeignClient {

  @PostMapping("/api/internal/products/list")
  Response<ListProductResponse> listProduct(@RequestBody ListProductRequest request);
}
