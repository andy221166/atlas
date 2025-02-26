package org.atlas.service.catalog.api.client.rest.resttemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.api.client.rest.resttemplate.RestTemplateService;
import org.atlas.service.catalog.api.client.CatalogApiClient;
import org.atlas.service.catalog.api.client.rest.model.ListProductResponse;
import org.atlas.service.catalog.port.inbound.internal.ListProductUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CatalogApiClientImpl implements CatalogApiClient {

  @Value("${app.api-client.rest.catalog-service.base-url:http://localhost:8082}")
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
