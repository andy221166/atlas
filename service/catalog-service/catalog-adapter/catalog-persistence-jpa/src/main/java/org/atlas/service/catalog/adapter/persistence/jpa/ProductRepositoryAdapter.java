package org.atlas.service.catalog.adapter.persistence.jpa;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.catalog.adapter.persistence.jpa.repository.JpaProductRepository;
import org.atlas.service.catalog.domain.entity.ProductEntity;
import org.atlas.service.catalog.port.outbound.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

  private final JpaProductRepository jpaProductRepository;

  @Override
  public List<ProductEntity> findByIdIn(List<Integer> ids) {
    return jpaProductRepository.findAllById(ids)
        .stream()
        .map(jpaProduct ->
            ObjectMapperUtil.getInstance().map(jpaProduct, ProductEntity.class))
        .toList();
  }

  @Override
  public Optional<ProductEntity> findById(Integer id) {
    return Optional.empty();
  }

  @Override
  public int insert(ProductEntity productEntity) {
    return 0;
  }

  @Override
  public int insertBatch(List<ProductEntity> productEntities) {
    return 0;
  }

  @Override
  public int update(ProductEntity productEntity) {
    return 0;
  }

  @Override
  public int updateBatch(List<ProductEntity> productEntities) {
    return 0;
  }

  @Override
  public int delete(ProductEntity productEntity) {
    return 0;
  }

  @Override
  public int deleteBatch(List<ProductEntity> productEntities) {
    return 0;
  }
}
