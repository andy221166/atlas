package org.atlas.platform.event.contract.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.service.order.contract.model.OrderDto;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReserveCreditSucceededEvent extends BaseOrderEvent {

  public ReserveCreditSucceededEvent(OrderDto order) {
    this.order = order;
  }

  @Override
  public String toString() {
    return "ReserveCreditSucceededEvent{" +
        "timestamp=" + getTimestamp() +
        ", eventId='" + getEventId() + '\'' +
        ", order=" + order +
        '}';
  }
}
