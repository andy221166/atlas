package org.atlas.service.order.contract.excel;

import org.atlas.service.order.domain.Order;

import java.util.List;

public interface OrderExcelWriter {

    byte[] write(List<Order> products, String sheetName) throws Exception;
}
