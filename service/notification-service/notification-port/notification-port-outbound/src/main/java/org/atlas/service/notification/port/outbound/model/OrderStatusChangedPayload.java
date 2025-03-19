package org.atlas.service.notification.port.outbound.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.service.order.domain.shared.OrderStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusChangedPayload {

  private Integer orderId;
  private OrderStatus orderStatus;
  private String canceledReason;
}