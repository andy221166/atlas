package org.atlas.service.product.port.inbound.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.product.domain.entity.ProductStatus;
import org.atlas.service.product.port.inbound.admin.GetProductUseCase.GetProductInput;
import org.atlas.service.product.port.inbound.admin.GetProductUseCase.GetProductOutput;

public interface GetProductUseCase
    extends UseCase<GetProductInput, GetProductOutput> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class GetProductInput {

    private Integer id;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class GetProductOutput {

    private Integer id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private Integer quantity;
    private ProductStatus status;
    private Date availableFrom;
    private Boolean isActive;
    private Brand brand;
    private ProductDetail detail;
    private List<ProductAttribute> attributes;
    private List<Category> categories;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Brand {

      private Integer id;
      private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDetail {

      private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductAttribute {

      private Integer id;
      private String attributeName;
      private String attributeValue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {

      private Integer id;
      private String name;
    }
  }
}
