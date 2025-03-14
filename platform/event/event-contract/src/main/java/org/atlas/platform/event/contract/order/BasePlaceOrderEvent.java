package org.atlas.platform.event.contract.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.order.model.OrderItem;
import org.atlas.platform.event.contract.order.model.User;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class BasePlaceOrderEvent extends DomainEvent {

  protected Integer orderId;
  protected User user;
  protected List<OrderItem> orderItems;
  protected BigDecimal amount;
  protected Date createdAt;

  public BasePlaceOrderEvent(String eventSource) {
    super(eventSource);
  }

  public void addOrderItem(OrderItem orderItem) {
    if (orderItems == null) {
      orderItems = new ArrayList<>();
    }
    this.orderItems.add(orderItem);
  }
}
