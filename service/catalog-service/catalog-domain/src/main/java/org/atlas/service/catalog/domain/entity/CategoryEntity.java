package org.atlas.service.catalog.domain.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.model.AuditableEntity;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class CategoryEntity extends AuditableEntity implements Serializable {

  @EqualsAndHashCode.Include
  private Integer id;
  private String name;
}
