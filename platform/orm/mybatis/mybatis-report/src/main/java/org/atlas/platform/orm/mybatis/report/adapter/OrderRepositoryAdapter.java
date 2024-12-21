package org.atlas.platform.orm.mybatis.report.adapter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.orm.mybatis.report.mapper.OrderMapper;
import org.atlas.service.report.contract.model.OrderDto;
import org.atlas.service.report.contract.model.ProductDto;
import org.atlas.service.report.contract.model.UserDto;
import org.atlas.service.report.contract.repository.OrderRepository;
import org.atlas.service.report.domain.Order;
import org.atlas.service.report.domain.OrderItem;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

  private final OrderMapper orderMapper;

  @Override
  public BigDecimal findTotalAmount(Date startDate, Date endDate) {
    return orderMapper.findTotalAmount(startDate, endDate);
  }

  @Override
  public List<OrderDto> findTopHighestAmountOrders(Date startDate, Date endDate, int limit) {
    return orderMapper.findTopHighestAmountOrders(startDate, endDate, limit);
  }

  @Override
  public List<ProductDto> findTopBestSoldProducts(Date startDate, Date endDate, int limit) {
    return orderMapper.findTopBestSoldProducts(startDate, endDate, limit);
  }

  @Override
  public List<UserDto> findTopHighestSpentUsers(Date startDate, Date endDate, int limit) {
    return orderMapper.findTopHighestSpentUsers(startDate, endDate, limit);
  }

  @Override
  public void insert(Order order) {
    // Insert the order and get the generated ID
    orderMapper.insertOrder(order);

    // Insert each order item
    for (OrderItem orderItem : order.getOrderItems()) {
      orderItem.setOrderId(order.getId());
      orderMapper.insertOrderItem(orderItem);
    }
  }
}
