package org.atlas.service.catalog.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.commons.model.AuditableEntity;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class ProductEntity extends AuditableEntity implements Serializable {

  @EqualsAndHashCode.Include
  private Integer id;
  private String code;
  private String name;
  private BigDecimal price;
  private ProductStatus status;
  private Date availableFrom;
  private Boolean isActive;

  // Associations
  // Many-To-One
  private BrandEntity brand;
  // One-To-One
  private ProductDetailEntity detail;
  // One-To-Many
  private List<ProductImageEntity> images;
  // Many-To-Many
  private List<CategoryEntity> categories;
}
