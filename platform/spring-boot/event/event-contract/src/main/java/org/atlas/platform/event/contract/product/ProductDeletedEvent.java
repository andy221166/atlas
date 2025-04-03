package org.atlas.platform.event.contract.product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.EventType;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductDeletedEvent extends DomainEvent {

  private Integer id;

  public ProductDeletedEvent(String eventSource) {
    super(eventSource);
  }
}
