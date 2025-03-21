package org.atlas.service.product.port.outbound.repository;

import java.util.List;
import java.util.Optional;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.service.product.domain.entity.ProductEntity;

public interface ProductRepositoryPort {

  PagingResult<ProductEntity> findAll(FindProductParams params,
      PagingRequest pagingRequest);

  List<ProductEntity> findByIdIn(List<Integer> ids);

  Optional<ProductEntity> findById(Integer id);

  void insert(ProductEntity productEntity);

  void update(ProductEntity productEntity);

  void decreaseQuantityWithConstraint(Integer id, Integer decrement);

  void decreaseQuantityWithPessimisticLock(Integer id, Integer decrement);

  void insertBatch(List<ProductEntity> productEntities);

  void delete(Integer id);
}
