package org.atlas.platform.persistence.mybatis.report.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.atlas.service.report.contract.model.OrderDto;
import org.atlas.service.report.contract.model.ProductDto;
import org.atlas.service.report.contract.model.UserDto;
import org.atlas.service.report.domain.Order;
import org.atlas.service.report.domain.OrderItem;

@Mapper
public interface OrderMapper {

  BigDecimal findTotalAmount(
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate
  );

  List<OrderDto> findTopHighestAmountOrders(
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate,
      @Param("limit") int limit
  );

  List<ProductDto> findTopBestSoldProducts(
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate,
      @Param("limit") int limit
  );

  List<UserDto> findTopHighestSpentUsers(
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate,
      @Param("limit") int limit
  );

  int insertOrder(Order order);

  int insertOrderItem(OrderItem orderItem);
}
