package org.atlas.platform.event.contract.product;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.EventType;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductUpdatedEvent extends DomainEvent {

  private Integer id;
  private String name;
  private BigDecimal price;

  public ProductUpdatedEvent(String eventSource) {
    super(eventSource);
  }

  @Override
  @NonNull
  public EventType getEventType() {
    return EventType.PRODUCT_UPDATED;
  }
}
