package org.atlas.platform.persistence.mybatis.task.mapper.order;

import java.util.Date;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.atlas.service.order.domain.shared.enums.OrderStatus;

@Mapper
public interface OrderMapper {

  int cancelOverdueProcessingOrders(
      @Param("canceledStatus") OrderStatus canceledStatus,
      @Param("canceledReason") String canceledReason,
      @Param("processingStatus") OrderStatus processingStatus,
      @Param("endDate") Date endDate);
}
