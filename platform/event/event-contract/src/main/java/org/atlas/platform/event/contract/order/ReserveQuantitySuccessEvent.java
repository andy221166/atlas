package org.atlas.platform.event.contract.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.service.order.contract.model.OrderDto;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReserveQuantitySuccessEvent extends BaseOrderEvent {

  public ReserveQuantitySuccessEvent(OrderDto order) {
    this.order = order;
  }

  @Override
  public String toString() {
    return "ReserveQuantitySuccessEvent{" +
        "timestamp=" + timestamp +
        ", eventId='" + eventId + '\'' +
        ", order=" + order +
        '}';
  }
}
