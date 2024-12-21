package org.atlas.platform.orm.jdbc.report.adapter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.orm.jdbc.report.repository.JdbcOrderRepository;
import org.atlas.service.report.contract.model.OrderDto;
import org.atlas.service.report.contract.model.ProductDto;
import org.atlas.service.report.contract.model.UserDto;
import org.atlas.service.report.contract.repository.OrderRepository;
import org.atlas.service.report.domain.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

  private final JdbcOrderRepository jdbcOrderRepository;

  @Override
  public BigDecimal findTotalAmount(Date startDate, Date endDate) {
    return jdbcOrderRepository.findTotalAmount(startDate, endDate);
  }

  @Override
  public List<OrderDto> findTopHighestAmountOrders(Date startDate, Date endDate, int limit) {
    return jdbcOrderRepository.findTopHighestAmountOrders(startDate, endDate, limit);
  }

  @Override
  public List<ProductDto> findTopBestSoldProducts(Date startDate, Date endDate, int limit) {
    return jdbcOrderRepository.findTopBestSoldProducts(startDate, endDate, limit);
  }

  @Override
  public List<UserDto> findTopHighestSpentUsers(Date startDate, Date endDate, int limit) {
    return jdbcOrderRepository.findTopHighestSpentUsers(startDate, endDate, limit);
  }

  @Override
  public void insert(Order order) {
    jdbcOrderRepository.insert(order);
  }
}
