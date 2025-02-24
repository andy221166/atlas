package org.atlas.service.catalog.adapter.persistence.mybatis;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.service.catalog.adapter.persistence.mybatis.mapper.ProductMapper;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.atlas.service.catalog.domain.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

  private final ProductMapper productMapper;

  @Override
  public List<ProductEntity> findByIdIn(List<Integer> ids) {
    return productMapper.findByIdIn(ids);
  }

  @Override
  public int insertBatch(List<ProductEntity> productEntities) {
    return productMapper.insertBatch(productEntities);
  }

  @Override
  public int decreaseQuantity(Integer id, Integer amount) {
    return productMapper.decreaseQuantity(id, amount);
  }
}
