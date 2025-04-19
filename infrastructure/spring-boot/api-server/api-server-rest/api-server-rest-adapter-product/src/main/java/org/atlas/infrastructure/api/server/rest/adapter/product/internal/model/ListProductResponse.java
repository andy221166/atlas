package org.atlas.infrastructure.api.server.rest.adapter.product.internal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "ApiResponseWrapper object containing the list of products.")
public class ListProductResponse {

  @Schema(description = "List of products retrieved from the request.", required = true)
  private List<Product> products;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Represents a product in the product list.")
  public static class Product {

    @Schema(description = "Unique identifier of the product.", example = "1")
    private Integer id;

    @Schema(description = "Name of the product.", example = "Sample SearchResponse")
    private String name;

    @Schema(description = "Price of the product.", example = "19.99")
    private BigDecimal price;
  }
}
