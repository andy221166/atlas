package org.atlas.service.catalog.port.outbound.repository;

import java.util.List;
import java.util.Optional;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.service.catalog.domain.entity.ProductEntity;

public interface ProductRepository {

  List<ProductEntity> findByIdIn(List<Integer> ids);

  PagingResult<ProductEntity> findByCriteria(FindProductCriteria criteria, PagingRequest pagingRequest);

  Optional<ProductEntity> findById(Integer id);

  int insert(ProductEntity productEntity);

  int insertBatch(List<ProductEntity> productEntities);

  int update(ProductEntity productEntity);

  int updateBatch(List<ProductEntity> productEntities);

  int delete(ProductEntity productEntity);

  int deleteBatch(List<ProductEntity> productEntities);
}
