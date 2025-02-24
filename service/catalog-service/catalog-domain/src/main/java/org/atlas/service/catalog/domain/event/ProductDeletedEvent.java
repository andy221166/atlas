package org.atlas.service.catalog.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.event.DomainEvent;
import org.atlas.platform.commons.event.EventType;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductDeletedEvent extends DomainEvent {

  private Integer id;

  public ProductDeletedEvent(String eventSource) {
    super(eventSource);
  }

  @Override
  @NonNull
  public EventType getEventType() {
    return EventType.PRODUCT_DELETED;
  }
}
