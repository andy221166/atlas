package org.atlas.platform.event.contract.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.contract.order.model.OrderItem;
import org.atlas.platform.event.contract.order.model.User;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderCanceledEvent extends DomainEvent {

  private Integer orderId;
  private User user;
  private List<OrderItem> orderItems;
  private BigDecimal amount;
  private Date createdAt;
  private String canceledReason;

  public OrderCanceledEvent(String eventSource) {
    super(eventSource);
  }

  @Override
  public EventType getEventType() {
    return EventType.ORDER_CANCELED;
  }
}
