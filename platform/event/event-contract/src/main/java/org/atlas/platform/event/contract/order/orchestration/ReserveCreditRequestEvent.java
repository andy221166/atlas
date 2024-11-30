package org.atlas.platform.event.contract.order.orchestration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.event.contract.order.BaseOrderEvent;
import org.atlas.service.order.contract.model.OrderDto;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReserveCreditRequestEvent extends BaseOrderEvent {

  public ReserveCreditRequestEvent(OrderDto order) {
    this.order = order;
  }

  @Override
  public String toString() {
    return "ReserveCreditRequestEvent{" +
        "timestamp=" + timestamp +
        ", eventId='" + eventId + '\'' +
        ", order=" + order +
        '}';
  }
}
