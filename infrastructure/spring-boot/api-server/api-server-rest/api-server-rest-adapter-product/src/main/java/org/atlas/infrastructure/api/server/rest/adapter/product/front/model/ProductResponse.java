package org.atlas.infrastructure.api.server.rest.adapter.product.front.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detailed information of a product found in the search.")
public class ProductResponse {

  @Schema(description = "Unique identifier of the product.", example = "123")
  private Integer id;

  @Schema(description = "Name of the product.", example = "T-Shirt")
  private String name;

  @Schema(description = "Price of the product.", example = "19.99")
  private BigDecimal price;

  @Schema(description = "The image of product.", example = "https://example.com/product-image.jpg")
  private String image;
}
