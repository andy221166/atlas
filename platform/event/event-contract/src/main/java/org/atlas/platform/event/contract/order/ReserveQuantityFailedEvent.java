package org.atlas.platform.event.contract.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.service.order.contract.model.OrderDto;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReserveQuantityFailedEvent extends BaseOrderEvent {

  private String error;

  public ReserveQuantityFailedEvent(OrderDto order, String error) {
    this.order = order;
    this.error = error;
  }

  @Override
  public String toString() {
    return "ReserveQuantityFailedEvent{" +
        "timestamp=" + timestamp +
        ", eventId='" + eventId + '\'' +
        ", order=" + order +
        ", error='" + error + '\'' +
        '}';
  }
}
