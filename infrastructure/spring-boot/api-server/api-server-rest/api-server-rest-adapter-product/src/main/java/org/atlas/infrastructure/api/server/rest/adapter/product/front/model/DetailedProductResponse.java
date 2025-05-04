package org.atlas.infrastructure.api.server.rest.adapter.product.front.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for retrieving product details.")
public class DetailedProductResponse {

  @Schema(description = "Unique identifier of the product.", example = "123")
  private Integer id;

  @Schema(description = "Name of the product.", example = "T-Shirt")
  private String name;

  @Schema(description = "Price of the product.", example = "19.99")
  private BigDecimal price;

  @Schema(description = "URL of the product's image.", example = "https://example.com/product-image.jpg")
  private String imageUrl;

  @Schema(description = "Description of the product.", example = "A comfortable cotton t-shirt.")
  private String description;

  @Schema(description = "Map containing the product attributes (key-value pairs).", example = "{\"Color\": \"Red\", \"Size\": \"M\"}")
  private Map<String, String> attributes;

  @Schema(description = "Brand of the product.", example = "Nike")
  private String brand;

  @Schema(description = "List of categories the product belongs to.", example = "[\"Apparel\", \"Clothing\"]")
  private List<String> categories;
}
