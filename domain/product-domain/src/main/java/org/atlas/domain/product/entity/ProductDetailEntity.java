package org.atlas.domain.product.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.atlas.framework.entity.DomainEntity;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class ProductDetailEntity extends DomainEntity {

  @EqualsAndHashCode.Include
  private Integer productId;
  private String description;
}
