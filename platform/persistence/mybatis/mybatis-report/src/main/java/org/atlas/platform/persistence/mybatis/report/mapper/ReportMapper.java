package org.atlas.platform.persistence.mybatis.report.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.atlas.service.report.contract.model.OrderDto;
import org.atlas.service.report.contract.model.ProductDto;
import org.atlas.service.report.contract.model.UserDto;

import java.util.Date;
import java.util.List;

@Mapper
public interface ReportMapper {

    List<OrderDto> findTopHighestAmountOrders(@Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate,
                                              @Param("limit") int limit);

    List<ProductDto> findTopBestSoldProducts(@Param("startDate") Date startDate,
                                             @Param("endDate") Date endDate,
                                             @Param("limit") int limit);

    List<UserDto> findTopHighestSpentUsers(@Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate,
                                           @Param("limit") int limit);
}
