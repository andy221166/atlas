package org.atlas.service.order.contract.repository;

import org.atlas.commons.util.paging.PageDto;
import org.atlas.commons.util.paging.PagingRequest;
import org.atlas.service.order.domain.Order;
import org.atlas.service.order.domain.shared.enums.OrderStatus;

import java.util.Optional;

public interface OrderRepository {

    PageDto<Order> findByUserId(Integer userId, PagingRequest pagingRequest);
    Optional<Order> findById(Integer id);
    void insert(Order order);
    void update(Order order);
}
