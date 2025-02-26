package org.atlas.service.catalog.port.inbound.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.catalog.domain.entity.ProductStatus;

public interface UpdateProductUseCase
    extends UseCase<UpdateProductUseCase.Input, Void> {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Input {

    @NotNull
    private Integer id;

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

      private Integer id;

      @NotBlank
      private String imageUrl;

      private Boolean isCover = false;
    }
  }
}
