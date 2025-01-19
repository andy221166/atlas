package org.atlas.platform.event.contract.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.order.payload.OrderPayload;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class BaseOrderEvent extends DomainEvent {

  protected OrderPayload orderPayload;
}
