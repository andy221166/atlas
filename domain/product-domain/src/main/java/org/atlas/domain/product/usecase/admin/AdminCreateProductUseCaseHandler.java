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
import org.atlas.domain.product.entity.ProductDetailsEntity;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.port.messaging.ProductMessagePublisherPort;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.domain.product.service.ProductImageService;
import org.atlas.domain.product.shared.enums.ProductStatus;
import org.atlas.domain.product.usecase.admin.AdminCreateProductUseCaseHandler.CreateProductInput;
import org.atlas.domain.product.usecase.admin.AdminCreateProductUseCaseHandler.CreateProductOutput;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.domain.event.contract.product.ProductCreatedEvent;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class AdminCreateProductUseCaseHandler implements
    UseCaseHandler<CreateProductInput, CreateProductOutput> {

  private final ProductRepository productRepository;
  private final ProductImageService productImageService;
  private final ApplicationConfigPort applicationConfigPort;
  private final ProductMessagePublisherPort productMessagePublisherPort;

  @Override
  public CreateProductOutput handle(CreateProductInput input) throws Exception {
    // Insert product into DB
    ProductEntity productEntity = map(input);
    productRepository.insert(productEntity);

    // Upload image
    productImageService.uploadImage(productEntity.getId(), input.getImage());

    // Publish event
    publishEvent(productEntity);

    Integer newProductId = productEntity.getId();
    return new CreateProductOutput(newProductId);
  }

  private ProductEntity map(CreateProductInput input) {
    // Product
    ProductEntity productEntity = new ProductEntity();
    productEntity.setName(input.getName());
    productEntity.setPrice(input.getPrice());
    productEntity.setQuantity(input.getQuantity());
    productEntity.setStatus(input.getStatus());
    productEntity.setAvailableFrom(input.getAvailableFrom());
    productEntity.setIsActive(input.getIsActive());

    // Brand
    BrandEntity brandEntity = new BrandEntity();
    brandEntity.setId(input.getBrandId());
    productEntity.setBrand(brandEntity);

    // Product details
    ProductDetailsEntity productDetailsEntity = ObjectMapperUtil.getInstance()
        .map(input.getDetails(), ProductDetailsEntity.class);
    productEntity.setDetails(productDetailsEntity);

    // Product attributes
    if (CollectionUtils.isNotEmpty(input.getAttributes())) {
      List<ProductAttributeEntity> productAttributeEntities = ObjectMapperUtil.getInstance()
          .mapList(input.getAttributes(), ProductAttributeEntity.class);
      productEntity.setAttributes(productAttributeEntities);
    }

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

    return productEntity;
  }

  private void publishEvent(ProductEntity productEntity) {
    ProductCreatedEvent event = new ProductCreatedEvent(applicationConfigPort.getApplicationName());
    ObjectMapperUtil.getInstance()
        .merge(productEntity, event);
    event.setProductId(productEntity.getId());
    productMessagePublisherPort.publish(event);
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CreateProductInput {

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal price;

    private String image;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    @NotNull
    private ProductStatus status;

    @NotNull
    private Date availableFrom;

    @NotNull
    private Boolean isActive;

    @NotNull
    private Integer brandId;

    @NotNull
    @Valid
    private ProductDetails details;

    @Valid
    private List<ProductAttribute> attributes;

    @NotEmpty
    private List<Integer> categoryIds;

    @Data
    public static class ProductDetails {

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
  public static class CreateProductOutput {

    private Integer id;
  }
}
