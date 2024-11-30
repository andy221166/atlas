package org.atlas.platform.persistence.mybatis.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.atlas.service.order.domain.OrderItem;

@Mapper
public interface OrderItemMapper {

  int insert(OrderItem orderItem);
}
