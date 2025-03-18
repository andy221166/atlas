package org.atlas.platform.event.gateway.outbox.model;

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
import org.atlas.platform.persistence.jpa.core.entity.JpaBaseEntity;

@Entity
@Table(name = "outbox_message")
@Getter
@Setter
public class OutboxMessage extends JpaBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String event;

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

  public void toBeProcessed() {
    this.status = OutboxMessageStatus.PROCESSED;
    this.processedAt = new Date();
  }

  public void incRetries() {
    retries++;
  }
}
