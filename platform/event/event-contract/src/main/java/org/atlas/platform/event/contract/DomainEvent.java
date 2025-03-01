package org.atlas.platform.event.contract;

import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.atlas.platform.commons.util.UUIDGenerator;

@NoArgsConstructor
@Getter
@Setter
public abstract class DomainEvent implements Serializable {

  private String eventId;
  private String eventSource;
  private Long timestamp;
  private Long version;

  public DomainEvent(String eventSource) {
    this.eventId = UUIDGenerator.generate();
    this.eventSource = eventSource;
    this.timestamp = Instant.now().toEpochMilli();
    this.version = 0L;
  }

  public abstract EventType getEventType();

  public void incrementVersion() {
    this.version++;
  }
}
