package org.atlas.domain.product.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.atlas.platform.commons.model.DomainEntity;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class BrandEntity extends DomainEntity {

  @EqualsAndHashCode.Include
  private Integer id;
  private String name;
}
