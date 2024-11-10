package org.atlas.platform.event.core.contract.order.choreography;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.event.core.EventType;
import org.atlas.platform.event.core.contract.order.BaseOrderEvent;
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
    public EventType type() {
        return EventType.RESERVE_QUANTITY_FAILED;
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
