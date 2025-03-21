package org.atlas.service.product.adapter.persistence.jpa;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.commons.enums.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.adapter.persistence.jpa.entity.JpaProductEntity;
import org.atlas.service.product.adapter.persistence.jpa.repository.CustomJpaProductRepository;
import org.atlas.service.product.adapter.persistence.jpa.repository.JpaProductRepository;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.port.outbound.repository.FindProductParams;
import org.atlas.service.product.port.outbound.repository.ProductRepositoryPort;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductRepositoryAdapter implements ProductRepositoryPort {

  private final JpaProductRepository jpaProductRepository;
  private final CustomJpaProductRepository customJpaProductRepository;

  @Override
  public PagingResult<ProductEntity> findAll(FindProductParams params,
      PagingRequest pagingRequest) {
    long totalCount = customJpaProductRepository.count(params);
    if (totalCount == 0L) {
      return PagingResult.empty();
    }
    List<JpaProductEntity> jpaProductEntities = customJpaProductRepository.find(params, pagingRequest);
    List<ProductEntity> productEntities = ObjectMapperUtil.getInstance()
        .mapList(jpaProductEntities, ProductEntity.class);
    return PagingResult.of(productEntities, totalCount);
  }

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
    return jpaProductRepository.findById(id)
        .map(jpaProduct -> ObjectMapperUtil.getInstance().map(jpaProduct, ProductEntity.class));
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
          .get();
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
