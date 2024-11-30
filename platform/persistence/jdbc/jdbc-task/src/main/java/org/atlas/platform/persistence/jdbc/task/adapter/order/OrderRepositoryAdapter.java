package org.atlas.platform.persistence.jdbc.task.adapter.order;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.jdbc.task.repository.order.JdbcOrderRepository;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.atlas.service.task.contract.core.CancelOverdueProcessingOrdersTask;
import org.atlas.service.task.contract.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

  private final JdbcOrderRepository jdbcOrderRepository;

  @Override
  public int cancelOverdueProcessingOrders(Date endDate) {
    return jdbcOrderRepository.cancelOverdueProcessingOrders(OrderStatus.CANCELED,
        CancelOverdueProcessingOrdersTask.CANCELED_REASON,
        OrderStatus.PROCESSING,
        endDate);
  }
}
