package org.atlas.platform.api.client.product.rest.feign;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.client.product.ProductApiClient;
import org.atlas.platform.api.client.product.rest.model.ListProductRequest;
import org.atlas.platform.api.client.product.rest.model.ListProductResponse;
import org.atlas.platform.api.client.rest.model.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase.ListProductInput;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase.ListProductOutput;
import org.springframework.stereotype.Component;

@Component
@Retry(name = "default-internal")
@CircuitBreaker(name = "default-internal")
@Bulkhead(name = "default-internal")
@RequiredArgsConstructor
public class ProductApiClientImpl implements ProductApiClient {

  private final ProductFeignClient feignClient;

  @Override
  public ListProductOutput call(ListProductInput input) {
    ListProductRequest request = ObjectMapperUtil.getInstance()
        .map(input, ListProductRequest.class);
    Response<ListProductResponse> response = feignClient.listProduct(request);
    ListProductResponse responseData = response.getData();
    return ObjectMapperUtil.getInstance()
        .map(responseData, ListProductOutput.class);
  }
}
