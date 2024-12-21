package org.atlas.platform.orm.jdbc.order.adapter;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.commons.util.paging.PagingRequest;
import org.atlas.platform.orm.jdbc.order.repository.JdbcOrderItemRepository;
import org.atlas.platform.orm.jdbc.order.repository.JdbcOrderRepository;
import org.atlas.service.order.contract.repository.OrderRepository;
import org.atlas.service.order.domain.Order;
import org.atlas.service.order.domain.OrderItem;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

  private final JdbcOrderRepository jdbcOrderRepository;
  private final JdbcOrderItemRepository jdbcOrderItemRepository;

  @Override
  public PageDto<Order> findByUserId(Integer userId, PagingRequest pagingRequest) {
    long totalCount = jdbcOrderRepository.countByUserId(userId);
    if (totalCount == 0L) {
      return PageDto.empty();
    }

    List<Order> products = jdbcOrderRepository.findByUserId(userId, pagingRequest);

    return PageDto.of(products, totalCount);
  }

  @Override
  public Optional<Order> findById(Integer id) {
    return jdbcOrderRepository.findById(id);
  }

  @Override
  public void insert(Order order) {
    jdbcOrderRepository.insert(order);
    for (OrderItem orderItem : order.getOrderItems()) {
      orderItem.setOrderId(order.getId());
      jdbcOrderItemRepository.insert(orderItem);
    }
  }

  @Override
  public void update(Order order) {
    jdbcOrderRepository.update(order);
  }
}
