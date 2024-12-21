package org.atlas.platform.orm.jpa.product.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.orm.jpa.product.entity.JpaProduct;
import org.atlas.service.product.domain.Category;
import org.atlas.service.product.domain.Product;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {

  public static Product map(JpaProduct jpaProduct) {
    Product product = ModelMapperUtil.map(jpaProduct, Product.class);
    if (jpaProduct.getCategory() != null) {
      Category category = ModelMapperUtil.map(jpaProduct.getCategory(), Category.class);
      product.setCategory(category);
    }
    return product;
  }
}
