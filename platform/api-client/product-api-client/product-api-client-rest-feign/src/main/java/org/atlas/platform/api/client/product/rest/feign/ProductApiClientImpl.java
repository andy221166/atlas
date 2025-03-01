package org.atlas.platform.api.client.product.rest.feign;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.client.product.ProductApiClient;
import org.atlas.platform.api.client.product.rest.model.ListProductResponse;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductApiClientImpl implements ProductApiClient {

  private final ProductFeignClient feignClient;

  @Override
  public ListProductUseCase.Output call(ListProductUseCase.Input input) {
    ListProductResponse response = feignClient.listProduct(input);
    return response.getData();
  }
}
