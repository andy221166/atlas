package org.atlas.platform.event.contract.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.event.contract.order.payload.OrderPayload;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderCreatedEvent extends BaseOrderEvent {

  public OrderCreatedEvent(OrderPayload orderPayload) {
    this.orderPayload = orderPayload;
  }
}
