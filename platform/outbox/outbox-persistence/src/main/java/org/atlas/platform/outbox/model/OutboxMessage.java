package org.atlas.platform.outbox.model;

import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.commons.model.Auditable;
import org.atlas.platform.event.contract.EventType;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class OutboxMessage extends Auditable {

  private Long id;
  private EventType eventType;
  private String payload;
  private OutboxMessageStatus status;
  private Date processedAt;
  private String error;
  private Integer retries = 0;

  public void toBeProcessed() {
    this.status = OutboxMessageStatus.PROCESSED;
    this.processedAt = new Date();
  }

  public void incRetries() {
    retries++;
  }
}
