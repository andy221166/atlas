package org.atlas.platform.api.client.product.rest.feign;

import org.atlas.platform.api.client.rest.feign.FeignConfig;
import org.atlas.platform.api.client.product.rest.model.ListProductResponse;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase;
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
  ListProductResponse listProduct(@RequestBody ListProductUseCase.Input input);
}
