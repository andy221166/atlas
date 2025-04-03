package org.atlas.service.product.application.admin;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.config.ApplicationConfigService;
import org.atlas.platform.event.contract.product.ProductCreatedEvent;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.domain.product.entity.BrandEntity;
import org.atlas.domain.product.entity.CategoryEntity;
import org.atlas.domain.product.entity.ProductAttributeEntity;
import org.atlas.domain.product.entity.ProductDetailEntity;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.port.inbound.product.admin.CreateProductUseCase;
import org.atlas.service.product.port.outbound.event.ProductEventPublisherPort;
import org.atlas.service.product.port.outbound.repository.ProductRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateProductUseCaseHandler implements CreateProductUseCase {

  private final ProductRepositoryPort productRepositoryPort;
  private final ApplicationConfigService applicationConfigService;
  private final ProductEventPublisherPort productEventPublisherPort;

  @Override
  @Transactional
  public CreateProductOutput handle(CreateProductInput input) throws Exception {
    // Insert product into DB
    ProductEntity productEntity = map(input);
    productRepositoryPort.insert(productEntity);

    // Publish event
    publishEvent(productEntity);

    Integer newProductId = productEntity.getId();
    return new CreateProductOutput(newProductId);
  }

  private ProductEntity map(CreateProductInput input) {
    // Product
    ProductEntity productEntity = ObjectMapperUtil.getInstance().map(input, ProductEntity.class);

    // Product detail
    ProductDetailEntity productDetailEntity = ObjectMapperUtil.getInstance()
        .map(input.getDetail(), ProductDetailEntity.class);
    productEntity.setDetail(productDetailEntity);

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

    // Brand
    BrandEntity brandEntity = new BrandEntity();
    brandEntity.setId(input.getBrandId());
    productEntity.setBrand(brandEntity);

    return productEntity;
  }

  private void publishEvent(ProductEntity productEntity) {
    ProductCreatedEvent event = new ProductCreatedEvent(
        applicationConfigService.getApplicationName());
    ObjectMapperUtil.getInstance().merge(productEntity, event);
    productEventPublisherPort.publish(event);
  }
}
