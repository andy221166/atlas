package org.atlas.infrastructure.api.server.rest.adapter.product.front.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.framework.paging.PagingRequest;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Request object for searching products with various filters.")
public class SearchProductRequest extends PagingRequest {

  @Schema(description = "Keyword for searching product names and descriptions.", example = "T-Shirt")
  private String keyword;

  @Schema(description = "Minimum price for filtering products.", example = "10.00")
  private BigDecimal minPrice;

  @Schema(description = "Maximum price for filtering products.", example = "100.00")
  private BigDecimal maxPrice;

  @Schema(description = "List of brand IDs to filter products by.", example = "[1, 2, 3]")
  private List<Integer> brandIds;

  @Schema(description = "List of category IDs to filter products by.", example = "[1, 2, 3]")
  private List<Integer> categoryIds;
}
