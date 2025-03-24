package org.atlas.service.product.domain.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.commons.model.DomainEntity;
import org.atlas.platform.commons.util.StringUtil;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class ProductEntity extends DomainEntity {

  @EqualsAndHashCode.Include
  private Integer id;
  private String code;
  private String name;
  private BigDecimal price;
  private Integer quantity;
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

  public String getImageUrl() {
    if (CollectionUtils.isEmpty(images)) {
      return StringUtil.EMPTY;
    }
    return images.stream()
        .filter(image -> Boolean.TRUE.equals(image.getIsCover()))
        .map(ProductImageEntity::getImageUrl)
        .findFirst()
        .orElse(images.get(0).getImageUrl());
  }
}
