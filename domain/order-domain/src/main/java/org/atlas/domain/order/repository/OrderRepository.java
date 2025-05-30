package org.atlas.domain.order.repository;

import java.util.Optional;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;

public interface OrderRepository {

  PagingResult<OrderEntity> findByCriteria(FindOrderCriteria criteria, PagingRequest pagingRequest);

  Optional<OrderEntity> findById(Integer id);

  void insert(OrderEntity orderEntity);

  void update(OrderEntity orderEntity);
}
