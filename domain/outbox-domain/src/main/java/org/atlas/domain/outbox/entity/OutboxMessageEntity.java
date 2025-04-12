package org.atlas.domain.outbox.entity;

import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.atlas.framework.entity.DomainEntity;
import org.atlas.framework.event.EventType;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class OutboxMessageEntity extends DomainEntity {

  @EqualsAndHashCode.Include
  private Long id;
  private String eventJson;
  private EventType eventType;
  private String destination;
  private OutboxMessageStatus status;
  private Date processedAt;
  private String error;
  private Integer retries = 0;

  public void markAsProcessed() {
    this.status = OutboxMessageStatus.PROCESSED;
    this.processedAt = new Date();
  }

  public void incRetries() {
    retries++;
  }
}
