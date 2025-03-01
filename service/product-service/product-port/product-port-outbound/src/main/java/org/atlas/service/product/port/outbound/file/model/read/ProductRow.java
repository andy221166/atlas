package org.atlas.service.product.port.outbound.file.model.read;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.atlas.service.product.domain.entity.ProductStatus;

@Data
public class ProductRow {

  private String name;
  private BigDecimal price;
  private Integer quantity;
  private ProductStatus status;
  private Date availableFrom;
  private Boolean isActive;
  private Integer brandId;
  private String description;
  private String imageUrl;
  private List<Integer> categoryIds;
}
