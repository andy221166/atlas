package org.atlas.platform.event.contract.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.event.contract.order.payload.OrderPayload;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReserveQuantityFailedEvent extends BaseOrderEvent {

  private String error;

  public ReserveQuantityFailedEvent(OrderPayload orderPayload, String error) {
    this.orderPayload = orderPayload;
    this.error = error;
  }
}
