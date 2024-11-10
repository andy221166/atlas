package org.atlas.service.product.contract.repository;

import org.atlas.service.product.domain.Product;

import java.util.List;

public interface ProductRepository {

    List<Product> findByIdIn(List<Integer> ids);
    int insertBatch(List<Product> products);
    int increaseQuantity(Integer id, Integer amount);
    int decreaseQuantity(Integer id, Integer amount);
}
