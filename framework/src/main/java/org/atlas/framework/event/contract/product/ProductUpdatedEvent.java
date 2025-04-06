package org.atlas.framework.event.contract.product;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.framework.event.DomainEvent;

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
}
