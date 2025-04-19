package org.atlas.infrastructure.api.server.rest.adapter.product.front.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.framework.paging.PagingResult;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "ApiResponseWrapper object for searching products with pagination.")
public class SearchProductResponse extends PagingResult<SearchProductResponse.Product> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Detailed information of a product found in the search.")
  public static class Product {

    @Schema(description = "Unique identifier of the product.", example = "123")
    private Integer id;

    @Schema(description = "Name of the product.", example = "T-Shirt")
    private String name;

    @Schema(description = "Price of the product.", example = "19.99")
    private BigDecimal price;

    @Schema(description = "URL of the product's image.", example = "https://example.com/product-image.jpg")
    private String imageUrl;
  }
}
