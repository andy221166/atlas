package org.atlas.domain.product.usecase.admin;

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
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.domain.product.entity.BrandEntity;
import org.atlas.domain.product.entity.CategoryEntity;
import org.atlas.domain.product.entity.ProductAttributeEntity;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.port.messaging.ProductMessagePublisherPort;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.domain.product.shared.enums.ProductStatus;
import org.atlas.domain.product.usecase.admin.AdminUpdateProductUseCaseHandler.UpdateProductInput;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.error.AppError;
import org.atlas.framework.event.contract.product.ProductUpdatedEvent;
import org.atlas.framework.exception.BusinessException;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class AdminUpdateProductUseCaseHandler implements UseCaseHandler<UpdateProductInput, Void> {

  private final ProductRepository productRepository;
  private final ApplicationConfigPort applicationConfigPort;
  private final ProductMessagePublisherPort productMessagePublisherPort;

  @Override
  public Void handle(UpdateProductInput input) throws Exception {
    ProductEntity productEntity = productRepository.findById(input.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));

    // Update product into DB
    merge(input, productEntity);
    productRepository.update(productEntity);

    // Publish event
    publishEvent(productEntity);

    return null;
  }

  private void merge(UpdateProductInput input, ProductEntity productEntity) {
    // SearchResponse
    ObjectMapperUtil.getInstance()
        .merge(input, productEntity);

    // SearchResponse detail
    ObjectMapperUtil.getInstance()
        .merge(input.getDetail(), productEntity.getDetail());

    // SearchResponse attributes
    if (CollectionUtils.isNotEmpty(input.getAttributes())) {
      List<ProductAttributeEntity> productAttributeEntities = ObjectMapperUtil.getInstance()
          .mapList(input.getAttributes(), ProductAttributeEntity.class);
      productEntity.setAttributes(productAttributeEntities);
    }

    // Brand
    BrandEntity brandEntity = new BrandEntity();
    brandEntity.setId(input.getBrandId());
    productEntity.setBrand(brandEntity);

    // Categories
    List<CategoryEntity> categoryEntities = input.getCategoryIds()
        .stream()
        .map(categoryId -> {
          CategoryEntity categoryEntity = new CategoryEntity();
          categoryEntity.setId(categoryId);
          return categoryEntity;
        })
        .toList();
    productEntity.setCategories(categoryEntities);
  }

  private void publishEvent(ProductEntity productEntity) {
    ProductUpdatedEvent event = new ProductUpdatedEvent(applicationConfigPort.getApplicationName());
    ObjectMapperUtil.getInstance()
        .merge(productEntity, event);
    event.setProductId(productEntity.getId());
    productMessagePublisherPort.publish(event);
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateProductInput {

    @NotNull
    private Integer id;

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin(value = "0.0")
    @Builder.Default
    private BigDecimal price = BigDecimal.ZERO;

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

      @NotNull
      private Integer id;

      @NotBlank
      private String name;

      @NotBlank
      private String value;
    }
  }
}
