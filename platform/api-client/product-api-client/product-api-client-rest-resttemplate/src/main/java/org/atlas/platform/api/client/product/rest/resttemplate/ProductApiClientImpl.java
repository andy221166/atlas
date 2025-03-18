package org.atlas.platform.api.client.product.rest.resttemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.api.client.rest.resttemplate.RestTemplateService;
import org.atlas.platform.api.client.product.rest.model.ListProductResponse;
import org.atlas.platform.api.client.product.ProductApiClient;
import org.atlas.service.product.port.inbound.usecase.internal.ListProductUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductApiClientImpl implements ProductApiClient {

  @Value("${app.api-client.rest.product-service.base-url:http://localhost:8082}")
  private String baseUrl;

  private final RestTemplateService service;

  @Override
  public ListProductUseCase.Output call(ListProductUseCase.Input input) {
    String url = String.format("%s/api/internal/products/list", baseUrl);

    ListProductResponse response =
        service.doPost(url, null, input, ListProductResponse.class);
    return response.getData();
  }
}
