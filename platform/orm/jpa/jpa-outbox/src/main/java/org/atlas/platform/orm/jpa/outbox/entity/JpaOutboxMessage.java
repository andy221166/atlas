package org.atlas.platform.orm.jpa.outbox.entity;

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
import org.atlas.platform.orm.jpa.core.entity.JpaBaseEntity;
import org.atlas.platform.outbox.model.OutboxMessageStatus;

@Entity
@Table(name = "outbox_message")
@Getter
@Setter
public class JpaOutboxMessage extends JpaBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String payload;

  @Enumerated(EnumType.STRING)
  private OutboxMessageStatus status;

  @Column(name = "processed_at")
  private Date processedAt;

  private String error;

  private Integer retries;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JpaOutboxMessage other = (JpaOutboxMessage) o;
    return id != null && id.equals(other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
