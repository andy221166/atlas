package org.atlas.platform.event.contract.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.event.contract.EventType;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderCreatedEvent extends BasePlaceOrderEvent {

  public OrderCreatedEvent(String eventSource) {
    super(eventSource);
  }

  @Override
  public EventType getEventType() {
    return EventType.ORDER_CREATED;
  }
}
