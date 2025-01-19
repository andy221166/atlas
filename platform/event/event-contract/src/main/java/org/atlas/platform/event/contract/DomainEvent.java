package org.atlas.platform.event.contract;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.atlas.commons.util.idgenerator.UUIDGenerator;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.CUSTOM,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    visible = true
)
@JsonTypeIdResolver(EventTypeResolver.class)
@Getter
@Setter
public abstract class DomainEvent implements Serializable {

  private final String id;
  private final EventType type;
  private String source;
  private final Long timestamp;
  private Long version;

  public DomainEvent() {
    this.id = UUIDGenerator.generate();
    this.type = EventTypeResolver.getEventTypeByClass(this.getClass());
    this.timestamp = Instant.now().toEpochMilli();
    this.version = 0L;
  }

  public void incrementVersion() {
    this.version++;
  }
}
