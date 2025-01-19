package org.atlas.platform.orm.mybatis.report.adapter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.orm.mybatis.report.mapper.OrderMapper;
import org.atlas.service.aggregator.contract.model.OrderDto;
import org.atlas.service.aggregator.contract.model.ProductDto;
import org.atlas.service.aggregator.contract.repository.aggregator.AggOrderRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AggOrderRepositoryAdapter implements AggOrderRepository {

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
  public void insert(AggOrder aggOrder) {
    // Insert the order and get the generated ID
    orderMapper.insertOrder(aggOrder);

    // Insert each order item
    for (AggOrderItem aggOrderItem : aggOrder.getOrderItemAggs()) {
      aggOrderItem.setOrderId(aggOrder.getId());
      orderMapper.insertOrderItem(aggOrderItem);
    }
  }
}
