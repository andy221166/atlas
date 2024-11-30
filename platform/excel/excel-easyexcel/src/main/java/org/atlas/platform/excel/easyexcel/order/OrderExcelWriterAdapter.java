package org.atlas.platform.excel.easyexcel.order;

import java.util.List;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.excel.easyexcel.core.EasyExcelWriter;
import org.atlas.service.order.contract.excel.OrderExcelWriter;
import org.atlas.service.order.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderExcelWriterAdapter implements OrderExcelWriter {

  @Override
  public byte[] write(List<Order> orders, String sheetName) throws Exception {
    List<OrderWriteModel> excelOrders = orders.stream()
        .map(order -> ModelMapperUtil.map(order, OrderWriteModel.class))
        .toList();
    return EasyExcelWriter.write(excelOrders, "Orders", OrderWriteModel.class);
  }
}
