package org.atlas.platform.event.core.contract.order.orchestration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.event.core.contract.order.BaseOrderEvent;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class BaseReplyEvent extends BaseOrderEvent {

    private boolean success;
    private String error;
}
