package org.atlas.service.product.adapter.api.server.rest.front.model;

import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.atlas.platform.commons.constant.Constant;
import org.checkerframework.checker.index.qual.Positive;

@Data
public class SearchProductRequest {

  private String keyword;

  private BigDecimal minPrice;

  private BigDecimal maxPrice;

  private List<Integer> brandIds;

  private List<Integer> categoryIds;

  @Positive
  @Min(0)
  private Integer page;

  @Positive
  @Min(0)
  private Integer size = Integer.valueOf(Constant.DEFAULT_PAGE_SIZE);
}
