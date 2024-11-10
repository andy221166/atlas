package org.atlas.platform.persistence.mybatis.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.atlas.commons.util.paging.PagingRequest;
import org.atlas.service.order.domain.Order;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<Order> findByUserId(@Param("userId") Integer userId, @Param("pageRequest") PagingRequest pagingRequest);
    Order findById(@Param("id") Integer id);
    long countByUserId(@Param("userId") Integer userId);
    int insert(Order order);
    int update(Order order);
}
