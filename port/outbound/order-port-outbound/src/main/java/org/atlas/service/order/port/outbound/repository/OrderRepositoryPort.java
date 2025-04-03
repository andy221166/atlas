package org.atlas.service.order.port.outbound.repository;

import java.util.Optional;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.domain.order.entity.OrderEntity;

public interface OrderRepositoryPort {

  PagingResult<OrderEntity> findAll(PagingRequest pagingRequest);

  PagingResult<OrderEntity> findByUserId(Integer userId, PagingRequest pagingRequest);

  Optional<OrderEntity> findById(Integer id);

  void insert(OrderEntity orderEntity);

  void update(OrderEntity orderEntity);
}
