package org.atlas.platform.excel.easyexcel.order;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import org.atlas.service.order.domain.shared.enums.OrderStatus;

public class OrderStatusConverter implements Converter<OrderStatus> {

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return CellDataTypeEnum.STRING;
  }

  @Override
  public WriteCellData<?> convertToExcelData(WriteConverterContext<OrderStatus> context)
      throws Exception {
    OrderStatus status = context.getValue();
    return new WriteCellData<>(status.name());
  }
}
