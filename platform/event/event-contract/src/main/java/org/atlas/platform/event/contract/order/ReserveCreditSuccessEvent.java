package org.atlas.platform.event.contract.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.service.order.contract.model.OrderDto;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReserveCreditSuccessEvent extends BaseOrderEvent {

  public ReserveCreditSuccessEvent(OrderDto order) {
    this.order = order;
  }

  @Override
  public String toString() {
    return "ReserveCreditSuccessEvent{" +
        "timestamp=" + getTimestamp() +
        ", eventId='" + getEventId() + '\'' +
        ", order=" + order +
        '}';
  }
}
