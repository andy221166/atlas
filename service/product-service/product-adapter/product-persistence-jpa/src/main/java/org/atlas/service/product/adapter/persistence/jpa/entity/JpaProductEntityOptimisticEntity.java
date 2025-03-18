package org.atlas.service.product.adapter.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Single Table Inheritance strategy:
 * JpaProductEntityOptimisticEntity inherits from JpaProductEntity and should not declare @Table.
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class JpaProductEntityOptimisticEntity extends JpaProductEntity {

    @Version // Enables Optimistic Locking
    private Integer version;
}
