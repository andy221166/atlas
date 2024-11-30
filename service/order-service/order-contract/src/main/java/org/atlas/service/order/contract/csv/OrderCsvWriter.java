package org.atlas.service.order.contract.csv;

import java.util.List;
import org.atlas.service.order.domain.Order;

public interface OrderCsvWriter {

  byte[] write(List<Order> orders) throws Exception;
}
