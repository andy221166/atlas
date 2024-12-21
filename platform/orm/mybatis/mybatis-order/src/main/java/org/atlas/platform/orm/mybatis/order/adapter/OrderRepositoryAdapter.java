package org.atlas.platform.orm.mybatis.order.adapter;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.commons.util.paging.PagingRequest;
import org.atlas.platform.orm.mybatis.order.mapper.OrderItemMapper;
import org.atlas.platform.orm.mybatis.order.mapper.OrderMapper;
import org.atlas.service.order.contract.repository.OrderRepository;
import org.atlas.service.order.domain.Order;
import org.atlas.service.order.domain.OrderItem;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

  private final OrderMapper orderMapper;
  private final OrderItemMapper orderItemMapper;

  @Override
  public PageDto<Order> findByUserId(Integer userId, PagingRequest pagingRequest) {
    long totalCount = orderMapper.countByUserId(userId);
    if (totalCount == 0L) {
      return PageDto.empty();
    }
    List<Order> products = orderMapper.findByUserId(userId, pagingRequest);
    return PageDto.of(products, totalCount);
  }

  @Override
  public Optional<Order> findById(Integer id) {
    return Optional.ofNullable(orderMapper.findById(id));
  }

  @Override
  public void insert(Order order) {
    orderMapper.insert(order);
    for (OrderItem orderItem : order.getOrderItems()) {
      orderItem.setOrderId(order.getId());
      orderItemMapper.insert(orderItem);
    }
  }

  @Override
  public void update(Order order) {
    orderMapper.update(order);
  }
}
