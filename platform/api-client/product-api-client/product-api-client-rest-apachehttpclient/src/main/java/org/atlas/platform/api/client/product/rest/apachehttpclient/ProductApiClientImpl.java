package org.atlas.platform.api.client.product.rest.apachehttpclient;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.client.product.ProductApiClient;
import org.atlas.platform.api.client.product.rest.model.ListProductRequest;
import org.atlas.platform.api.client.product.rest.model.ListProductResponse;
import org.atlas.platform.api.client.rest.apachehttpclient.HttpClientService;
import org.atlas.platform.api.client.rest.model.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase.ListProductInput;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase.ListProductOutput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductApiClientImpl implements ProductApiClient {

  @Value("${app.api-client.rest.product-service.base-url:http://localhost:8082}")
  private String baseUrl;

  private final HttpClientService service;

  @Override
  @SuppressWarnings("unchecked")
  public ListProductOutput call(ListProductInput input) {
    String url = String.format("%s/api/internal/products/list", baseUrl);
    ListProductRequest request = ObjectMapperUtil.getInstance()
        .map(input, ListProductRequest.class);
    Response<ListProductResponse> response =
        service.doPost(url, null, request, Response.class);
    ListProductResponse responseData = response.getData();
    return ObjectMapperUtil.getInstance()
        .map(responseData, ListProductOutput.class);
  }
}
