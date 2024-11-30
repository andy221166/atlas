package org.atlas.platform.persistence.mybatis.task.adapter.order;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.mybatis.task.mapper.order.OrderMapper;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.atlas.service.task.contract.core.CancelOverdueProcessingOrdersTask;
import org.atlas.service.task.contract.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

  private final OrderMapper orderMapper;

  @Override
  public int cancelOverdueProcessingOrders(Date endDate) {
    return orderMapper.cancelOverdueProcessingOrders(OrderStatus.CANCELED,
        CancelOverdueProcessingOrdersTask.CANCELED_REASON,
        OrderStatus.PROCESSING,
        endDate);
  }
}
