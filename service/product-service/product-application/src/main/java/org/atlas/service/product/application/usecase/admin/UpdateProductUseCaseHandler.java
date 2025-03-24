package org.atlas.service.product.application.usecase.admin;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.commons.enums.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.config.ApplicationConfigService;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.BrandEntity;
import org.atlas.service.product.domain.entity.CategoryEntity;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.domain.entity.ProductAttributeEntity;
import org.atlas.platform.event.contract.product.ProductUpdatedEvent;
import org.atlas.service.product.port.inbound.usecase.admin.UpdateProductUseCase;
import org.atlas.service.product.port.outbound.event.ProductEventPublisherPort;
import org.atlas.service.product.port.outbound.repository.ProductRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateProductUseCaseHandler implements UpdateProductUseCase {

  private final ProductRepositoryPort productRepositoryPort;
  private final ApplicationConfigService applicationConfigService;
  private final ProductEventPublisherPort productEventPublisherPort;

  @Override
  @Transactional
  public Void handle(Input input) throws Exception {
    ProductEntity productEntity = productRepositoryPort.findById(input.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));

    // Update product into DB
    merge(input, productEntity);
    productRepositoryPort.update(productEntity);

    // Publish event
    publishEvent(productEntity);

    return null;
  }

  private void merge(Input input, ProductEntity productEntity) {
    // Product
    ObjectMapperUtil.getInstance().merge(input, productEntity);

    // Product detail
    ObjectMapperUtil.getInstance().merge(input.getDetail(), productEntity.getDetail());

    // Product attributes
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
    ProductUpdatedEvent event = new ProductUpdatedEvent(
        applicationConfigService.getApplicationName());
    ObjectMapperUtil.getInstance().merge(productEntity, event);
    productEventPublisherPort.publish(event);
  }
}
