package org.atlas.platform.api.client.rest.feign.core;

import org.atlas.platform.rest.client.contract.order.ListOrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "order-service",
    url = "${app.rest-client.order-service.base-url:http://localhost:8081}",
    configuration = FeignConfig.class
)
public interface OrderFeignClient {

  @GetMapping("/api/orders")
  ListOrderResponse listOrder(
      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "20") Integer size);
}
