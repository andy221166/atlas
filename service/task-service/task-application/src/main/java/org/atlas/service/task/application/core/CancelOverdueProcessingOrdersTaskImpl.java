package org.atlas.service.task.application.core;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.service.task.contract.core.CancelOverdueProcessingOrdersTask;
import org.atlas.service.task.contract.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CancelOverdueProcessingOrdersTaskImpl implements CancelOverdueProcessingOrdersTask {

  private static final int PROCESSING_TIMEOUT = 60 * 60 * 1000; // 60 minutes

  private final OrderRepository orderRepository;

  @Override
  @Transactional(transactionManager = "orderTransactionManager")
  public void execute(Object... params) {
    Date now = new Date();
    Date endDate = new Date(now.getTime() - PROCESSING_TIMEOUT);
    int canceled = orderRepository.cancelOverdueProcessingOrders(endDate);
    log.info("Canceled {} overdue processing orders", canceled);
  }
}
