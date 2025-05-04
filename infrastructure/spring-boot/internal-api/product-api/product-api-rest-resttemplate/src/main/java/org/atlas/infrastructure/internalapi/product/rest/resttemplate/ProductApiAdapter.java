package org.atlas.infrastructure.internalapi.product.rest.resttemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.shared.internal.ListProductInput;
import org.atlas.domain.product.shared.internal.ListProductOutput;
import org.atlas.framework.api.server.rest.response.ApiResponseWrapper;
import org.atlas.framework.internalapi.ProductApiPort;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.api.client.rest.resttemplate.RestTemplateService;
import org.atlas.infrastructure.internalapi.product.rest.model.ListProductRequest;
import org.atlas.infrastructure.internalapi.product.rest.model.ListProductResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Retry(name = "default")
@CircuitBreaker(name = "default")
@Bulkhead(name = "default")
@RequiredArgsConstructor
public class ProductApiAdapter implements ProductApiPort {

  @Value("${app.api-client.rest.product-service.base-url:http://localhost:8082}")
  private String baseUrl;

  private final RestTemplateService service;

  @Override
  @SuppressWarnings("unchecked")
  public ListProductOutput call(ListProductInput input) {
    String url = String.format("%s/api/internal/products/list", baseUrl);
    ListProductRequest request = ObjectMapperUtil.getInstance()
        .map(input, ListProductRequest.class);
    ApiResponseWrapper<ListProductResponse> apiResponseWrapper =
        service.doPost(url, null, request, ApiResponseWrapper.class);
    ListProductResponse responseData = apiResponseWrapper.getData();
    return ObjectMapperUtil.getInstance()
        .map(responseData, ListProductOutput.class);
  }
}
