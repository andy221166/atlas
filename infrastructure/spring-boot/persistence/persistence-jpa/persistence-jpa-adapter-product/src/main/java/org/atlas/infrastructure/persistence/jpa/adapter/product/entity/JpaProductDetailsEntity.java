package org.atlas.infrastructure.persistence.jpa.adapter.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.atlas.infrastructure.persistence.jpa.core.entity.JpaBaseEntity;

@Entity
@Table(name = "product_details")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class JpaProductDetailsEntity extends JpaBaseEntity {

  @Id
  @Column(name = "product_id")
  @EqualsAndHashCode.Include
  private Integer productId;

  private String description;
}
