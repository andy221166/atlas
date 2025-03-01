package org.atlas.service.order.port.outbound.repository;

import java.util.Optional;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.commons.util.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.service.order.domain.entity.OrderEntity;

public interface OrderRepository {

  PagingResult<OrderEntity> findByUserId(Integer userId, PagingRequest pagingRequest);

  Optional<OrderEntity> findById(Integer id);

  void insert(OrderEntity orderEntity);

  void update(OrderEntity orderEntity);
}
