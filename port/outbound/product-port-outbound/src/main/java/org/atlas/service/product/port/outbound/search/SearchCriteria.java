package org.atlas.service.product.port.outbound.search;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class SearchCriteria {

  private String keyword;
  private BigDecimal minPrice;
  private BigDecimal maxPrice;
  private List<Integer> brandIds;
  private List<Integer> categoryIds;
}
