package org.atlas.service.catalog.adapter.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.atlas.platform.persistence.jpa.core.entity.JpaBaseEntity;

@Entity
@Table(name = "product_detail")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class JpaProductDetail extends JpaBaseEntity {

  @Id
  @Column(name = "product_id")
  @EqualsAndHashCode.Include
  private Integer productId;

  private String description;
}
