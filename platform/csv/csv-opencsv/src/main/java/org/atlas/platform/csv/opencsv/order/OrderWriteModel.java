package org.atlas.platform.csv.opencsv.order;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import org.atlas.service.order.domain.shared.enums.OrderStatus;

@Data
public class OrderWriteModel {

  @CsvBindByName(column = "Order ID")
  @CsvBindByPosition(position = 0)
  private Integer id;

  @CsvBindByName(column = "Amount")
  @CsvBindByPosition(position = 1)
  private BigDecimal amount;

  @CsvBindByName(column = "Status")
  @CsvBindByPosition(position = 2)
  private OrderStatus status;

  @CsvBindByName(column = "Canceled Reason")
  @CsvBindByPosition(position = 3)
  private String canceledReason;

  @CsvBindByName(column = "Created At")
  @CsvBindByPosition(position = 4)
  @CsvDate("yyyy-MM-dd HH:mm:ss")
  private Date createdAt;
}