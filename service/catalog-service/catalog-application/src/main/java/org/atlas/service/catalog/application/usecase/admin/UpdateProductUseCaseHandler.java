package org.atlas.service.catalog.application.usecase.admin;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.commons.exception.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.catalog.domain.entity.BrandEntity;
import org.atlas.service.catalog.domain.entity.CategoryEntity;
import org.atlas.service.catalog.domain.entity.ProductEntity;
import org.atlas.service.catalog.domain.entity.ProductImageEntity;
import org.atlas.service.catalog.domain.event.ProductUpdatedEvent;
import org.atlas.service.catalog.port.inbound.admin.UpdateProductUseCase;
import org.atlas.service.catalog.port.outbound.event.publisher.ProductEventPublisher;
import org.atlas.service.catalog.port.outbound.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateProductUseCaseHandler implements UpdateProductUseCase {

  private final ProductRepository productRepository;
  private final ProductEventPublisher productEventPublisher;

  @Value("${spring.application.name}")
  private String applicationName;

  @Override
  @Transactional
  public Void handle(Input input) throws Exception {
    ProductEntity productEntity = productRepository.findById(input.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));

    // Update product into DB
    merge(input, productEntity);
    productRepository.update(productEntity);

    // Publish event
    publishEvent(productEntity);

    return null;
  }

  private void merge(Input input, ProductEntity productEntity) {
    // Product
    ObjectMapperUtil.getInstance().merge(input, productEntity);

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

    // Product detail
    ObjectMapperUtil.getInstance().merge(input.getDetail(), productEntity.getDetail());

    // Product images
    if (CollectionUtils.isNotEmpty(input.getImages())) {
      List<ProductImageEntity> productImageEntities = ObjectMapperUtil.getInstance()
          .mapList(input.getImages(), ProductImageEntity.class);
      productEntity.setImages(productImageEntities);
    }
  }

  private void publishEvent(ProductEntity productEntity) {
    ProductUpdatedEvent event = new ProductUpdatedEvent(applicationName);
    ObjectMapperUtil.getInstance().merge(productEntity, event);
    productEventPublisher.publish(event);
  }
}
