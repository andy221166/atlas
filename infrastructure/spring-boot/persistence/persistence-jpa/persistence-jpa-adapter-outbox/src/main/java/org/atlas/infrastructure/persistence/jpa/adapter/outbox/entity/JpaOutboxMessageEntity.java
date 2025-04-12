package org.atlas.infrastructure.persistence.jpa.adapter.outbox.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.atlas.domain.outbox.entity.OutboxMessageStatus;
import org.atlas.framework.event.EventType;
import org.atlas.infrastructure.persistence.jpa.core.entity.JpaBaseEntity;

@Entity
@Table(name = "outbox_message")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class JpaOutboxMessageEntity extends JpaBaseEntity {

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

  public void markAsProcessed() {
    this.status = OutboxMessageStatus.PROCESSED;
    this.processedAt = new Date();
  }

  public void incRetries() {
    retries++;
  }
}
