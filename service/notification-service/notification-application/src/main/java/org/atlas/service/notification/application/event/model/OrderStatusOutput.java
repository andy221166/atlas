package org.atlas.service.notification.application.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.service.order.domain.shared.OrderStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusOutput {

  private OrderStatus orderStatus;
  private String canceledReason;

  public OrderStatusOutput(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }
}
