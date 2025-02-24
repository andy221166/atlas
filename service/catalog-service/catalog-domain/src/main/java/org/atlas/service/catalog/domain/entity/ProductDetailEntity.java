package org.atlas.service.catalog.domain.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.commons.model.AuditableEntity;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class ProductDetailEntity extends AuditableEntity implements Serializable {

  @EqualsAndHashCode.Include
  private Integer productId;
  private String description;
}
