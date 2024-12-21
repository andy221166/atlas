package org.atlas.platform.orm.jpa.task.adapter.order;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.orm.jpa.task.repository.order.JpaOrderRepository;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.atlas.service.task.contract.core.CancelOverdueProcessingOrdersTask;
import org.atlas.service.task.contract.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

  private final JpaOrderRepository jpaOrderRepository;

  @Override
  public int cancelOverdueProcessingOrders(Date endDate) {
    return jpaOrderRepository.cancelOverdueProcessingOrders(OrderStatus.CANCELED,
        CancelOverdueProcessingOrdersTask.CANCELED_REASON,
        OrderStatus.PROCESSING,
        endDate);
  }
}
