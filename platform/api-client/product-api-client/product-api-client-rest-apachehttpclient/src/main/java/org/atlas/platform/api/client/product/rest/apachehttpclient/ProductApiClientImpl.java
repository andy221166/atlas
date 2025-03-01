package org.atlas.platform.api.client.product.rest.apachehttpclient;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.client.rest.apachehttpclient.HttpClientService;
import org.atlas.platform.api.client.product.ProductApiClient;
import org.atlas.platform.api.client.product.rest.model.ListProductResponse;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductApiClientImpl implements ProductApiClient {

  @Value("${app.api-client.rest.product-service.base-url:http://localhost:8082}")
  private String baseUrl;

  private final HttpClientService service;

  @Override
  public ListProductUseCase.Output call(ListProductUseCase.Input input) {
    String url = String.format("%s/api/internal/products/list", baseUrl);

    ListProductResponse response = service.doPost(url, null, input, ListProductResponse.class);
    return response.getData();
  }
}
