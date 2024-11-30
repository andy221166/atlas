package org.atlas.platform.event.contract.order.orchestration;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReserveCreditReplyEvent extends BaseReplyEvent {

  @Override
  public String toString() {
    return "ReserveCreditReplyEvent{" +
        "timestamp=" + timestamp +
        ", eventId='" + eventId + '\'' +
        ", order=" + order +
        '}';
  }
}
