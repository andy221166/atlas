package org.atlas.service.report.contract.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.atlas.service.report.contract.model.OrderDto;
import org.atlas.service.report.contract.model.ProductDto;
import org.atlas.service.report.contract.model.UserDto;
import org.atlas.service.report.domain.Order;

public interface OrderRepository {

  BigDecimal findTotalAmount(Date startDate, Date endDate);

  List<OrderDto> findTopHighestAmountOrders(Date startDate, Date endDate, int limit);

  List<ProductDto> findTopBestSoldProducts(Date startDate, Date endDate, int limit);

  List<UserDto> findTopHighestSpentUsers(Date startDate, Date endDate, int limit);

  void insert(Order order);
}
