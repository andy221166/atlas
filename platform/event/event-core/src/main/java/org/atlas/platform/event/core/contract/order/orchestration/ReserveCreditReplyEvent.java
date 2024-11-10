package org.atlas.platform.event.core.contract.order.orchestration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.event.core.EventType;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReserveCreditReplyEvent extends BaseReplyEvent {

    @Override
    public EventType type() {
        return EventType.RESERVE_CREDIT_REPLY;
    }

    @Override
    public String toString() {
        return "ReserveCreditReplyEvent{" +
            "timestamp=" + timestamp +
            ", eventId='" + eventId + '\'' +
            ", order=" + order +
            '}';
    }
}
