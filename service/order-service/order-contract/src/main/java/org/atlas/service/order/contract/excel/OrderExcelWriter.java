package org.atlas.service.order.contract.excel;

import java.util.List;
import org.atlas.service.order.domain.Order;

public interface OrderExcelWriter {

  byte[] write(List<Order> products, String sheetName) throws Exception;
}
