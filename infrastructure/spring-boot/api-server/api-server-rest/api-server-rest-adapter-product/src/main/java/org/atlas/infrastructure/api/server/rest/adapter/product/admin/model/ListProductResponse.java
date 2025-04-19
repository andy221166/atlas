package org.atlas.infrastructure.api.server.rest.adapter.product.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.domain.product.shared.enums.ProductStatus;
import org.atlas.framework.paging.PagingResult;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "ApiResponseWrapper object for listing products with pagination.")
public class ListProductResponse extends PagingResult<ListProductResponse.Product> {

  @Data
  @Schema(description = "Summary information of a product.")
  public static class Product {

    @Schema(description = "Unique identifier of the product.", example = "123")
    private Integer id;

    @Schema(description = "Name of the product.", example = "T-Shirt")
    private String name;

    @Schema(description = "URL of the product's image.", example = "https://example.com/product-image.jpg")
    private String imageUrl;

    @Schema(description = "Price of the product.", example = "19.99")
    private BigDecimal price;

    @Schema(description = "Quantity of the product available.", example = "100")
    private Integer quantity;

    @Schema(description = "Status of the product.", example = "IN_STOCK")
    private ProductStatus status;

    @Schema(description = "Date and time the product becomes available in ISO 8601 format.", example = "2023-10-01T10:00:00Z")
    private Date availableFrom;

    @Schema(description = "Indicates if the product is active.", example = "true")
    private Boolean isActive;
  }
}
