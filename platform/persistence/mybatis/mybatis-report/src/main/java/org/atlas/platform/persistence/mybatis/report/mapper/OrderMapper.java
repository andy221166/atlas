package org.atlas.platform.persistence.mybatis.report.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.atlas.service.report.domain.Order;
import org.atlas.service.report.domain.OrderItem;

@Mapper
public interface OrderMapper {

    int insertOrder(Order order);
    int insertOrderItem(OrderItem orderItem);
}
