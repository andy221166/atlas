package org.atlas.service.product.port.outbound.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.atlas.service.product.domain.entity.ProductStatus;

@Data
public class FindProductCriteria {

  private Integer id;
  private String keyword;
  private BigDecimal minPrice;
  private BigDecimal maxPrice;
  private ProductStatus status;
  private Date availableFrom;
  private Boolean isActive;
  private Integer brandId;
  private List<Integer> categoryIds;
}
