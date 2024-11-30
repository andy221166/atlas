package org.atlas.platform.persistence.mybatis.product.adapter.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.mybatis.product.mapper.ProductMapper;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

  private final ProductMapper productMapper;

  @Override
  public List<Product> findByIdIn(List<Integer> ids) {
    return productMapper.findByIdIn(ids);
  }

  @Override
  public int insertBatch(List<Product> products) {
    return productMapper.insertBatch(products);
  }

  @Override
  public int increaseQuantity(Integer id, Integer amount) {
    return productMapper.increaseQuantity(id, amount);
  }

  @Override
  public int decreaseQuantity(Integer id, Integer amount) {
    return productMapper.decreaseQuantity(id, amount);
  }
}
