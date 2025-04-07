package org.atlas.infrastructure.event.gateway.outbox.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.atlas.framework.event.EventType;
import org.atlas.infrastructure.persistence.jpa.core.entity.JpaBaseEntity;

@Entity
@Table(name = "outbox_message")
@Getter
@Setter
public class OutboxMessage extends JpaBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "event_json")
  private String eventJson;

  @Column(name = "event_type")
  @Enumerated(EnumType.STRING)
  private EventType eventType;

  private String destination;

  @Enumerated(EnumType.STRING)
  private OutboxMessageStatus status;

  @Column(name = "processed_at")
  private Date processedAt;

  private String error;

  private Integer retries = 0;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OutboxMessage other = (OutboxMessage) o;
    return id != null && id.equals(other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public void markAsProcessed() {
    this.status = OutboxMessageStatus.PROCESSED;
    this.processedAt = new Date();
  }

  public void incRetries() {
    retries++;
  }
}
