package org.atlas.platform.api.client.contract.product;

import org.atlas.service.product.contract.model.ProductDto;

import java.util.List;

public interface IProductServiceClient {

    List<ProductDto> listProduct(List<Integer> ids);
}
