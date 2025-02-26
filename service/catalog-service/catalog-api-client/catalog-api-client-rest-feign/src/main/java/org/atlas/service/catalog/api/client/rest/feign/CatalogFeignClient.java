package org.atlas.service.catalog.api.client.rest.feign;

import org.atlas.platform.api.client.rest.feign.FeignConfig;
import org.atlas.service.catalog.api.client.rest.model.ListProductResponse;
import org.atlas.service.catalog.port.inbound.internal.ListProductUseCase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "catalog-service",
    url = "${app.api-client.rest.catalog-service.base-url:http://localhost:8082}",
    configuration = FeignConfig.class
)
public interface CatalogFeignClient {

  @PostMapping("/api/internal/products/list")
  ListProductResponse listProduct(@RequestBody ListProductUseCase.Input input);
}
