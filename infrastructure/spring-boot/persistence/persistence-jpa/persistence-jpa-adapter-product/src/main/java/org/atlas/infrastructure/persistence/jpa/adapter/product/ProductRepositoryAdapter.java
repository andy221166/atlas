package org.atlas.infrastructure.persistence.jpa.adapter.product;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.repository.FindProductCriteria;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.framework.error.AppError;
import org.atlas.framework.exception.BusinessException;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.infrastructure.persistence.jpa.adapter.product.entity.JpaProductEntity;
import org.atlas.infrastructure.persistence.jpa.adapter.product.repository.CustomJpaProductRepository;
import org.atlas.infrastructure.persistence.jpa.adapter.product.repository.JpaProductRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductRepositoryAdapter implements ProductRepository {

  private final JpaProductRepository jpaProductRepository;
  private final CustomJpaProductRepository customJpaProductRepository;

  @Override
  public PagingResult<ProductEntity> findByCriteria(FindProductCriteria criteria,
      PagingRequest pagingRequest) {
    long totalCount = customJpaProductRepository.countByCriteria(criteria);
    if (totalCount == 0L) {
      return PagingResult.empty();
    }
    List<JpaProductEntity> jpaProductEntities = customJpaProductRepository.findByCriteria(criteria,
        pagingRequest);
    List<ProductEntity> productEntities = ObjectMapperUtil.getInstance()
        .mapList(jpaProductEntities, jpaProductEntity -> {
          // Just map selected columns
          ProductEntity productEntity = new ProductEntity();
          productEntity.setId(jpaProductEntity.getId());
          productEntity.setName(jpaProductEntity.getName());
          productEntity.setPrice(jpaProductEntity.getPrice());
          productEntity.setImageUrl(jpaProductEntity.getImageUrl());
          return productEntity;
        });
    return PagingResult.of(productEntities, totalCount, pagingRequest);
  }

  @Override
  public List<ProductEntity> findByIdIn(List<Integer> ids) {
    return jpaProductRepository.findAllById(ids)
        .stream()
        .map(jpaProduct -> ObjectMapperUtil.getInstance()
            .map(jpaProduct, ProductEntity.class))
        .toList();
  }

  @Override
  public Optional<ProductEntity> findById(Integer id) {
    return jpaProductRepository.findByIdWithAssociations(id)
        .map(jpaProduct -> ObjectMapperUtil.getInstance()
            .map(jpaProduct, ProductEntity.class));
  }

  @Override
  public void insert(ProductEntity productEntity) {
    JpaProductEntity jpaProductEntity = ObjectMapperUtil.getInstance()
        .map(productEntity, JpaProductEntity.class);
    jpaProductRepository.save(jpaProductEntity);
    productEntity.setId(jpaProductEntity.getId());
  }

  @Override
  public void update(ProductEntity productEntity) {
    JpaProductEntity jpaProductEntity = ObjectMapperUtil.getInstance()
        .map(productEntity, JpaProductEntity.class);
    jpaProductRepository.save(jpaProductEntity);
  }

  @Override
  public void decreaseQuantityWithConstraint(Integer id, Integer decrement) {
    int updated = jpaProductRepository.decreaseQuantity(id, decrement);
    if (updated == 0) {
      throw new BusinessException(AppError.PRODUCT_INSUFFICIENT_QUANTITY);
    }
  }

  @Override
  public void decreaseQuantityWithPessimisticLock(Integer id, Integer decrement) {
    try {
      JpaProductEntity product = jpaProductRepository.findByIdWithLock(id)
          .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));
      if (product.getQuantity() < decrement) {
        throw new BusinessException(AppError.PRODUCT_INSUFFICIENT_QUANTITY);
      }
      product.setQuantity(product.getQuantity() - decrement);
      jpaProductRepository.save(product);
    } catch (Exception e) {
      throw new RuntimeException(
          String.format("Error while decreasing quantity: id=%d, decrement=%d",
              id, decrement), e);
    }
  }

  @Override
  public void insertBatch(List<ProductEntity> productEntities) {
    List<JpaProductEntity> jpaProductEntities = ObjectMapperUtil.getInstance()
        .mapList(productEntities, JpaProductEntity.class);
    jpaProductRepository.saveAll(jpaProductEntities);
  }

  @Override
  public void delete(Integer id) {
    jpaProductRepository.deleteById(id);
  }
}
