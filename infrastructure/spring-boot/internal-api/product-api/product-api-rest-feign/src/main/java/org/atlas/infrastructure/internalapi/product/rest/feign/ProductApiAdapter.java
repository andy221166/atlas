package org.atlas.infrastructure.internalapi.product.rest.feign;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.shared.internal.ListProductInput;
import org.atlas.domain.product.shared.internal.ListProductOutput;
import org.atlas.framework.internalapi.ProductApiPort;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.api.client.core.rest.model.Response;
import org.atlas.infrastructure.internalapi.product.rest.model.ListProductRequest;
import org.atlas.infrastructure.internalapi.product.rest.model.ListProductResponse;
import org.springframework.stereotype.Component;

@Component
@Retry(name = "default-internal-api")
@CircuitBreaker(name = "default-internal-api")
@Bulkhead(name = "default-internal-api")
@RequiredArgsConstructor
public class ProductApiAdapter implements ProductApiPort {

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
