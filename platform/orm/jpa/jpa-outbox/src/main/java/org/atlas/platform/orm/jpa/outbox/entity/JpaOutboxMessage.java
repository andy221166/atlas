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
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.outbox.model.OutboxMessageStatus;
import org.atlas.platform.orm.jpa.core.entity.JpaBaseEntity;

@Entity
@Table(name = "outbox_message")
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
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
}
