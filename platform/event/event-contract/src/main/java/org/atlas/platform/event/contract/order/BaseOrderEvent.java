package org.atlas.platform.event.contract.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.service.order.contract.model.OrderDto;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class BaseOrderEvent extends DomainEvent {

  protected OrderDto order;
}
