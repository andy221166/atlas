package org.atlas.platform.rest.client.feign.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.api.client.contract.product.IProductServiceClient;
import org.atlas.platform.rest.client.contract.product.ListProductResponse;
import org.atlas.platform.rest.client.feign.client.ProductFeignClient;
import org.atlas.service.product.contract.model.ProductDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceClient implements IProductServiceClient {

  private final ProductFeignClient feignClient;

  @Override
  public List<ProductDto> listProduct(List<Integer> ids) {
    ListProductResponse response = feignClient.listProduct(ids);
    return response.getData();
  }
}
