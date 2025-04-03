package org.atlas.domain.product.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.atlas.platform.commons.model.DomainEntity;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class ProductEntity extends DomainEntity {

  @EqualsAndHashCode.Include
  private Integer id;
  private String name;
  private String imageUrl;
  private BigDecimal price;
  private Integer quantity;
  private ProductStatus status;
  private Date availableFrom;
  private Boolean isActive;

  // Associations
  // One-To-One
  private ProductDetailEntity detail;
  // One-To-Many
  private List<ProductAttributeEntity> attributes;
  // Many-To-One
  private BrandEntity brand;
  // Many-To-Many
  private List<CategoryEntity> categories;

  public void addAttribute(ProductAttributeEntity attribute) {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    attributes.add(attribute);
  }

  public void addCategory(CategoryEntity category) {
    if (categories == null) {
      categories = new ArrayList<>();
    }
    categories.add(category);
  }
}
