package org.atlas.service.catalog.api.client.rest.feign;

import lombok.RequiredArgsConstructor;
import org.atlas.service.catalog.api.client.CatalogApiClient;
import org.atlas.service.catalog.api.client.rest.model.ListProductResponse;
import org.atlas.service.catalog.port.inbound.internal.ListProductUseCase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CatalogApiClientImpl implements CatalogApiClient {

  private final CatalogFeignClient feignClient;

  @Override
  public ListProductUseCase.Output call(ListProductUseCase.Input input) {
    ListProductResponse response = feignClient.listProduct(input);
    return response.getData();
  }
}
