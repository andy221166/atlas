package org.atlas.platform.event.contract.order.choreography;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.event.contract.order.BaseOrderEvent;
import org.atlas.service.order.contract.model.OrderDto;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CreditReservedEvent extends BaseOrderEvent {

  public CreditReservedEvent(OrderDto order) {
    this.order = order;
  }

  @Override
  public String toString() {
    return "CreditReservedEvent{" +
        "timestamp=" + timestamp +
        ", eventId='" + eventId + '\'' +
        ", order=" + order +
        '}';
  }
}