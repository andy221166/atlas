package org.atlas.service.catalog.port.outbound.file.model.write;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.atlas.service.catalog.domain.entity.ProductStatus;

@Data
public class ProductRow {

  private Integer id;
  private String code;
  private String name;
  private BigDecimal price;
  private ProductStatus status;
  private Date availableFrom;
  private Boolean isActive;
  private Integer brandId;
  private String description;
  private String imageUrl;
  private List<Integer> categoryIds;
}
