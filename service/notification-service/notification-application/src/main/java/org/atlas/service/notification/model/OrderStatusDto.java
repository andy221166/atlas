package org.atlas.service.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.service.order.domain.shared.enums.OrderStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusDto {

  private OrderStatus orderStatus;
  private String canceledReason;

  public OrderStatusDto(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }
}
