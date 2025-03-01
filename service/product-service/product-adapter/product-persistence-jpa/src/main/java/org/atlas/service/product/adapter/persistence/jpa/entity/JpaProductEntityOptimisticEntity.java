package org.atlas.service.product.adapter.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class JpaProductEntityOptimisticEntity extends JpaProductEntity {

    @Version // Enables Optimistic Locking
    private Integer version;
}
