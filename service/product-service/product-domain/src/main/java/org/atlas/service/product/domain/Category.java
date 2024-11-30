package org.atlas.service.product.domain;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.commons.model.Auditable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Category extends Auditable implements Serializable {

  @EqualsAndHashCode.Include
  private Integer id;
  private String name;

  public Category(Integer id) {
    this.id = id;
  }
}
