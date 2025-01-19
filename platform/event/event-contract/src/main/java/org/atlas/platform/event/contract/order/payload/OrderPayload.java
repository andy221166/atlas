package org.atlas.platform.event.contract.order.payload;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.atlas.service.order.domain.shared.enums.OrderStatus;

@Data
public class OrderPayload {

  private Integer id;
  private Integer userId;
  private List<OrderItemPayload> orderItems;
  private BigDecimal amount;
  private OrderStatus status;
  private Date createdAt;
  private String canceledReason;

  public void addOrderItem(OrderItemPayload orderItemPayload) {
    if (orderItems == null) {
      orderItems = new ArrayList<>();
    }
    this.orderItems.add(orderItemPayload);
  }
}
