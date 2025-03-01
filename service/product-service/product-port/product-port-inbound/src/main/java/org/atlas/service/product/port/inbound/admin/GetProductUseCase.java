package org.atlas.service.product.port.inbound.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.product.domain.entity.ProductStatus;

public interface GetProductUseCase
    extends UseCase<GetProductUseCase.Input, GetProductUseCase.Output> {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Input {

    private Integer id;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

    private Product product;

    @Data
    public static class Product {

      private Integer id;
      private String code;
      private String name;
      private BigDecimal price;
      private ProductStatus status;
      private Date availableFrom;
      private Boolean isActive;
      private Brand brand;
      private ProductDetail detail;
      private List<ProductImage> images;
      private List<Category> categories;
    }

    @Data
    public static class Brand {

      private Integer id;
      private String name;
    }

    @Data
    public static class ProductDetail {

      private String description;
    }

    @Data
    public static class ProductImage {

      private Integer id;
      private String imageUrl;
      private Boolean isCover;
    }

    @Data
    public static class Category {

      private Integer id;
      private String name;
    }
  }
}
