package org.atlas.service.product.application.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.service.product.contract.model.CategoryDto;
import org.atlas.service.product.contract.model.ProductDto;
import org.atlas.service.product.domain.Product;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {

  public static ProductDto map(Product product) {
    ProductDto productDto = ModelMapperUtil.map(product, ProductDto.class);
    CategoryDto categoryDto = ModelMapperUtil.map(product.getCategory(), CategoryDto.class);
    productDto.setCategory(categoryDto);
    return productDto;
  }
}
