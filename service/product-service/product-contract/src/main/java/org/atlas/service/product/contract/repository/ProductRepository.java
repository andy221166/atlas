package org.atlas.service.product.contract.repository;

import java.util.List;
import org.atlas.service.product.domain.Product;

public interface ProductRepository {

  List<Product> findByIdIn(List<Integer> ids);

  int insertBatch(List<Product> products);

  int increaseQuantity(Integer id, Integer amount);

  int decreaseQuantity(Integer id, Integer amount);
}
