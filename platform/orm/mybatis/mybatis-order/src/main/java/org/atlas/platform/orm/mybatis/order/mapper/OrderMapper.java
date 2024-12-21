package org.atlas.platform.orm.mybatis.order.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.atlas.commons.util.paging.PagingRequest;
import org.atlas.service.order.domain.Order;

@Mapper
public interface OrderMapper {

  List<Order> findByUserId(@Param("userId") Integer userId,
      @Param("pageRequest") PagingRequest pagingRequest);

  Order findById(@Param("id") Integer id);

  long countByUserId(@Param("userId") Integer userId);

  int insert(Order order);

  int update(Order order);
}
