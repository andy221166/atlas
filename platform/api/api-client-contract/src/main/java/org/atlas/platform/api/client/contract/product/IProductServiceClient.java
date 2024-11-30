package org.atlas.platform.api.client.contract.product;

import java.util.List;
import org.atlas.service.product.contract.model.ProductDto;

public interface IProductServiceClient {

  List<ProductDto> listProduct(List<Integer> ids);
}
