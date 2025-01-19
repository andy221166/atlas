package org.atlas.platform.orm.mybatis.report.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.atlas.service.aggregator.contract.model.OrderDto;
import org.atlas.service.aggregator.contract.model.ProductDto;

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

  int insertOrder(AggOrder aggOrder);

  int insertOrderItem(AggOrderItem aggOrderItem);
}
