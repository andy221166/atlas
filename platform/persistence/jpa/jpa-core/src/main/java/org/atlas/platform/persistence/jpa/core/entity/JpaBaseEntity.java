package org.atlas.platform.persistence.jpa.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
