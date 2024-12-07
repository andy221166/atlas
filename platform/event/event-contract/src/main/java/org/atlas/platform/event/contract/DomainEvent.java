package org.atlas.platform.event.contract;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import org.atlas.commons.util.idgenerator.UUIDGenerator;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.CUSTOM,
    include = JsonTypeInfo.As.PROPERTY,
    property = "eventType",
    visible = true
)
@JsonTypeIdResolver(EventTypeResolver.class)
@Getter
public abstract class DomainEvent implements Serializable {

  private final String eventId;
  private final EventType eventType;
  private final Long timestamp;

  public DomainEvent() {
    this.eventId = UUIDGenerator.generate();
    this.timestamp = Instant.now().toEpochMilli();
    this.eventType = EventTypeResolver.getEventTypeByClass(this.getClass());
  }
}
