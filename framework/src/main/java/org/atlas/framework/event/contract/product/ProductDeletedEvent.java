package org.atlas.framework.event.contract.product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.framework.event.DomainEvent;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductDeletedEvent extends DomainEvent {

  private Integer productId;

  public ProductDeletedEvent(String eventSource) {
    super(eventSource);
  }
}
