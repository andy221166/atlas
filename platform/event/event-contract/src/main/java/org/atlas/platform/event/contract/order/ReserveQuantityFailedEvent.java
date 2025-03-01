package org.atlas.platform.event.contract.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.event.contract.EventType;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReserveQuantityFailedEvent extends BaseOrderEvent {

  private String error;

  public ReserveQuantityFailedEvent(String eventSource) {
    super(eventSource);
  }

  @Override
  public EventType getEventType() {
    return EventType.RESERVE_QUANTITY_FAILED;
  }
}
