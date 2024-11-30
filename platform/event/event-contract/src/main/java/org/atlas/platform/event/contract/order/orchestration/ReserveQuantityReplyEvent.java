package org.atlas.platform.event.contract.order.orchestration;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReserveQuantityReplyEvent extends BaseReplyEvent {

  @Override
  public String toString() {
    return "ReserveQuantityReplyEvent{" +
        "timestamp=" + timestamp +
        ", eventId='" + eventId + '\'' +
        ", order=" + order +
        '}';
  }
}
