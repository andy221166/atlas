package org.atlas.platform.api.client.rest.feign.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.api.client.contract.product.IProductServiceClient;
import org.atlas.platform.rest.client.contract.product.ListProductBulkResponse;
import org.atlas.platform.api.client.rest.feign.core.ProductFeignClient;
import org.atlas.service.product.contract.model.ProductDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceClient implements IProductServiceClient {

  private final ProductFeignClient feignClient;

  @Override
  public List<ProductDto> listProductBulk(List<Integer> ids) {
    ListProductBulkResponse response = feignClient.listProductBulk(ids);
    return response.getData();
  }
}
