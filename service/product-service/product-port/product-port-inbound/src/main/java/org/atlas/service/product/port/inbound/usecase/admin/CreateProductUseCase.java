package org.atlas.service.product.port.inbound.usecase.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.product.domain.entity.ProductStatus;

public interface CreateProductUseCase
    extends UseCase<CreateProductUseCase.Input, CreateProductUseCase.Output> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class Input {

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal price = BigDecimal.ZERO;

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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDetail {

      @NotBlank
      private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductImage {

      @NotBlank
      private String imageUrl;

      private Boolean isCover = false;
    }
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

    private Integer id;
  }
}
