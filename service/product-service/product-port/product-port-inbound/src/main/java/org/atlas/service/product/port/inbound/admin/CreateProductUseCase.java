package org.atlas.service.product.port.inbound.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.product.domain.entity.ProductStatus;
import org.atlas.service.product.port.inbound.admin.CreateProductUseCase.CreateProductInput;
import org.atlas.service.product.port.inbound.admin.CreateProductUseCase.CreateProductOutput;

public interface CreateProductUseCase extends UseCase<CreateProductInput, CreateProductOutput> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class CreateProductInput {

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal price;

    private String imageUrl;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

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
    private List<ProductAttribute> attributes;

    @NotEmpty
    private List<Integer> categoryIds;

    @Data
    public static class ProductDetail {

      @NotBlank
      private String description;
    }

    @Data
    public static class ProductAttribute {

      @NotBlank
      private String name;

      @NotBlank
      private String value;
    }
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class CreateProductOutput {

    private Integer id;
  }
}
