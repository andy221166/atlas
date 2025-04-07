package org.atlas.domain.product.usecase.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.domain.product.entity.CategoryEntity;
import org.atlas.domain.product.entity.ProductAttributeEntity;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.domain.product.usecase.front.FrontGetProductUseCaseHandler.GetProductInput;
import org.atlas.domain.product.usecase.front.FrontGetProductUseCaseHandler.GetProductOutput;
import org.atlas.framework.cache.CachePort;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.error.AppError;
import org.atlas.framework.exception.BusinessException;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class FrontGetProductUseCaseHandler implements
    UseCaseHandler<GetProductInput, GetProductOutput> {

  private final ProductRepository productRepository;
  private final ApplicationConfigPort applicationConfigPort;
  private final CachePort cachePort;

  @Override
  public GetProductOutput handle(GetProductInput input) throws Exception {
    ProductEntity productEntity = productRepository.findById(input.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));
    GetProductOutput output = map(productEntity);
    cachePort.set(applicationConfigPort.getProductCacheName(),
        String.valueOf(productEntity.getId()),
        output, Duration.ofHours(1));
    return output;
  }

  private GetProductOutput map(ProductEntity productEntity) {
    GetProductOutput product = new GetProductOutput();
    product.setId(productEntity.getId());
    product.setName(productEntity.getName());
    product.setPrice(productEntity.getPrice());
    product.setImageUrl(productEntity.getImageUrl());
    product.setDescription(productEntity.getDetail() != null ?
        productEntity.getDetail().getDescription() : null);
    if (CollectionUtils.isNotEmpty(productEntity.getAttributes())) {
      product.setAttributes(productEntity.getAttributes()
          .stream()
          .collect(Collectors.toMap(ProductAttributeEntity::getName,
              ProductAttributeEntity::getValue)));
    }
    product.setBrand(productEntity.getBrand() != null ?
        productEntity.getBrand().getName() : null);
    if (CollectionUtils.isNotEmpty(productEntity.getCategories())) {
      product.setCategories(productEntity.getCategories()
          .stream()
          .map(CategoryEntity::getName)
          .toList());
    }
    return product;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetProductInput {

    private Integer id;
  }

  /**
   * Implement {@link Serializable} to support caching
   */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetProductOutput implements Serializable {

    private Integer id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private String description;
    private Map<String, String> attributes;
    private String brand;
    private List<String> categories;
  }
}
