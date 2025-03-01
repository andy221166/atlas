package org.atlas.service.product.adapter.api.server.rest.admin.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.atlas.service.product.domain.entity.ProductStatus;

@Data
public class CreateProductRequest {

  @NotBlank
  private String name;

  @NotNull
  @DecimalMin(value = "0.0")
  private BigDecimal price;

  @NotNull
  private ProductStatus status;

  @NotNull
  private Date availableFrom;

  private Boolean isActive;

  @NotNull
  private Integer brandId;

  @NotNull
  @Valid
  private ProductDetail detail;

  @Valid
  private List<ProductImage> images;

  @NotEmpty
  private List<Integer> categoryIds;

  @Data
  public static class ProductDetail {

    @NotBlank
    private String description;
  }

  @Data
  public static class ProductImage {

    @NotBlank
    private String imageUrl;

    private Boolean isCover = false;
  }
}