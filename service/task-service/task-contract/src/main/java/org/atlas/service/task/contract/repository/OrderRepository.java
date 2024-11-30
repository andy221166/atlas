package org.atlas.service.task.contract.repository;

import java.util.Date;

public interface OrderRepository {

  int cancelOverdueProcessingOrders(Date endDate);
}
