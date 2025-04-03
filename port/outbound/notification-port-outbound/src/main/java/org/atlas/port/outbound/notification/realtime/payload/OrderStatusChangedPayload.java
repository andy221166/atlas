package org.atlas.port.outbound.notification.realtime.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.domain.order.shared.OrderStatus;

@Data
@NoArgsConstructor
public class OrderStatusChangedPayload {

  private Integer orderId;
  private OrderStatus orderStatus;
  private String canceledReason;

  public static OrderStatusChangedPayload from(OrderConfirmedEvent event) {
    OrderStatusChangedPayload instance = new OrderStatusChangedPayload();
    instance.setOrderId(event.getOrderId());
    instance.setOrderStatus(OrderStatus.CONFIRMED);
    return instance;
  }

  public static OrderStatusChangedPayload from(OrderCanceledEvent event) {
    OrderStatusChangedPayload instance = new OrderStatusChangedPayload();
    instance.setOrderId(event.getOrderId());
    instance.setOrderStatus(OrderStatus.CANCELED);
    instance.setCanceledReason(event.getCanceledReason());
    return instance;
  }
}