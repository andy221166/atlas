package org.atlas.service.product.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.atlas.platform.commons.model.DomainEntity;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class ProductAttributeEntity extends DomainEntity {

  @EqualsAndHashCode.Include
  private Integer id;
  private Integer productId;
  private String name;
  private String value;
}
