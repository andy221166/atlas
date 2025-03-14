package org.atlas.service.order.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.commons.model.DomainEntity;
import org.atlas.service.order.domain.shared.OrderStatus;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class OrderEntity extends DomainEntity {

  @EqualsAndHashCode.Include
  private Integer id;
  private String code;
  private Integer userId;
  private List<OrderItemEntity> orderItems;
  private BigDecimal amount;
  private OrderStatus status;
  private String canceledReason;

  public void addOrderItem(OrderItemEntity orderItem) {
    if (orderItems == null) {
      orderItems = new ArrayList<>();
    }
    this.orderItems.add(orderItem);
  }

  public void calculateOrderAmount() {
    this.amount = BigDecimal.ZERO;
    if (CollectionUtils.isEmpty(orderItems)) {
      return;
    }
    for (OrderItemEntity orderItem : orderItems) {
      this.amount = this.amount.add(
          orderItem.getProductPrice().multiply(new BigDecimal(orderItem.getQuantity())));
    }
  }
}
