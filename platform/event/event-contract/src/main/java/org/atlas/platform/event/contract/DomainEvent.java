package org.atlas.platform.event.contract;

import java.io.Serializable;
import java.time.Instant;
import lombok.Data;
import org.atlas.commons.util.idgenerator.UUIDGenerator;

@Data
public abstract class DomainEvent implements Serializable {

  protected String eventId;
  protected Long timestamp;

  protected DomainEvent() {
    this.eventId = UUIDGenerator.generate();
    this.timestamp = Instant.now().toEpochMilli();
  }
}
