package org.atlas.service.order.contract.repository;

import java.util.Optional;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.commons.util.paging.PagingRequest;
import org.atlas.service.order.domain.Order;

public interface OrderRepository {

  PageDto<Order> findByUserId(Integer userId, PagingRequest pagingRequest);

  Optional<Order> findById(Integer id);

  void insert(Order order);

  void update(Order order);
}
