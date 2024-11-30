package org.atlas.service.order.contract.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.atlas.service.user.contract.model.UserDto;

@Data
public class OrderDto {

  private Integer id;
  private UserDto user;
  private List<OrderItemDto> orderItems;
  private BigDecimal amount;
  private OrderStatus status;
  private Date createdAt;
  private String canceledReason;

  public void addOrderItem(OrderItemDto orderItem) {
    if (orderItems == null) {
      orderItems = new ArrayList<>();
    }
    this.orderItems.add(orderItem);
  }
}
