package org.atlas.platform.persistence.jpa.task.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class JpaBaseEntity {

  @Column(name = "created_at", insertable = false)
  @Temporal(TemporalType.TIMESTAMP)
  protected Date createdAt;

  @Column(name = "updated_at", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  protected Date updatedAt;
}
