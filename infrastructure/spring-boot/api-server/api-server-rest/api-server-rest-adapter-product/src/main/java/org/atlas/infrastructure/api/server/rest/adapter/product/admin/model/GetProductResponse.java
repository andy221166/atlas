package org.atlas.infrastructure.api.server.rest.adapter.product.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.domain.product.shared.enums.ProductStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for retrieving product details.")
public class GetProductResponse {

  @Schema(description = "Unique identifier of the product.", example = "123")
  private Integer id;

  @Schema(description = "Name of the product.", example = "T-Shirt")
  private String name;

  @Schema(description = "Price of the product.", example = "19.99")
  private BigDecimal price;

  @Schema(description = "Image of the product.", example = "https://example.com/product-image.jpg")
  private String image;

  @Schema(description = "Quantity of the product available.", example = "100")
  private Integer quantity;

  @Schema(description = "Status of the product.", example = "IN_STOCK")
  private ProductStatus status;

  @Schema(description = "Date and time the product becomes available in ISO 8601 format.", example = "2023-10-01T10:00:00Z")
  private Date availableFrom;

  @Schema(description = "Indicates if the product is active.", example = "true")
  private Boolean isActive;

  @Schema(description = "Brand information of the product.")
  private Brand brand;

  @Schema(description = "Detailed information about the product.")
  private ProductDetails details;

  @Schema(description = "List of attributes associated with the product.")
  private List<ProductAttribute> attributes;

  @Schema(description = "List of categories the product belongs to.")
  private List<Category> categories;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Information about the product's brand.")
  public static class Brand {

    @Schema(description = "Unique identifier of the brand.", example = "1")
    private Integer id;

    @Schema(description = "Name of the brand.", example = "Nike")
    private String name;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Detailed information about the product.")
  public static class ProductDetails {

    @Schema(description = "Description of the product.", example = "A comfortable cotton t-shirt.")
    private String description;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Attributes associated with the product.")
  public static class ProductAttribute {

    @Schema(description = "Unique identifier of the product attribute.", example = "1")
    private Integer id;

    @Schema(description = "Name of the product attribute.", example = "Color")
    private String name;

    @Schema(description = "Value of the product attribute.", example = "Red")
    private String value;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Category information related to the product.")
  public static class Category {

    @Schema(description = "Unique identifier of the category.", example = "2")
    private Integer id;

    @Schema(description = "Name of the category.", example = "Apparel")
    private String name;
  }
}
