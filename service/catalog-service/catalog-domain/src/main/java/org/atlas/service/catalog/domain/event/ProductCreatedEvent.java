package org.atlas.service.catalog.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.enums.Role;
import org.atlas.platform.commons.event.DomainEvent;
import org.atlas.platform.commons.event.EventType;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductCreatedEvent extends DomainEvent {

  private Integer id;
  private String name;

  public ProductCreatedEvent(String eventSource) {
    super(eventSource);
  }

  @Override
  @NonNull
  public EventType getEventType() {
    return EventType.PRODUCT_CREATED;
  }
}
