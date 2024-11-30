package org.atlas.service.product.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.commons.model.Auditable;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Product extends Auditable implements Serializable {

  @EqualsAndHashCode.Include
  private Integer id;
  private String name;
  private String description;
  private BigDecimal price;
  private Integer quantity;
  private Category category;
}
