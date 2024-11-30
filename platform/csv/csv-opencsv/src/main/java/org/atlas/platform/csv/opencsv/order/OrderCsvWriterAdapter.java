package org.atlas.platform.csv.opencsv.order;

import java.util.List;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.csv.opencsv.core.OpenCsvWriter;
import org.atlas.service.order.contract.csv.OrderCsvWriter;
import org.atlas.service.order.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderCsvWriterAdapter implements OrderCsvWriter {

  @Override
  public byte[] write(List<Order> orders) throws Exception {
    List<OrderWriteModel> csvOrders = orders.stream()
        .map(order -> ModelMapperUtil.map(order, OrderWriteModel.class))
        .toList();
    return OpenCsvWriter.write(csvOrders, OrderWriteModel.class);
  }
}
