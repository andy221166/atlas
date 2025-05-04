package org.atlas.domain.product.usecase.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.domain.product.shared.enums.ProductStatus;
import org.atlas.domain.product.usecase.admin.AdminGetProductUseCaseHandler.GetProductInput;
import org.atlas.domain.product.usecase.admin.AdminGetProductUseCaseHandler.GetProductOutput;
import org.atlas.framework.error.AppError;
import org.atlas.framework.exception.DomainException;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class AdminGetProductUseCaseHandler implements UseCaseHandler<GetProductInput, GetProductOutput> {

  private final ProductRepository productRepository;

  @Override
  public GetProductOutput handle(GetProductInput input) throws Exception {
    ProductEntity productEntity = productRepository.findById(input.getId())
        .orElseThrow(() -> new DomainException(AppError.PRODUCT_NOT_FOUND));
    return ObjectMapperUtil.getInstance()
        .map(productEntity, GetProductOutput.class);
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetProductInput {

    private Integer id;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetProductOutput {

    private Integer id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private Integer quantity;
    private ProductStatus status;
    private Date availableFrom;
    private Boolean isActive;
    private Brand brand;
    private ProductDetails details;
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
    public static class ProductDetails {

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
