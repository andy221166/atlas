package org.atlas.service.product.application.usecase.admin;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.platform.sequencegenerator.SequenceGenerator;
import org.atlas.service.product.domain.entity.BrandEntity;
import org.atlas.service.product.domain.entity.CategoryEntity;
import org.atlas.service.product.domain.entity.ProductDetailEntity;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.domain.entity.ProductImageEntity;
import org.atlas.platform.event.contract.product.ProductCreatedEvent;
import org.atlas.service.product.port.inbound.admin.CreateProductUseCase;
import org.atlas.service.product.port.outbound.event.publisher.ProductEventPublisher;
import org.atlas.service.product.port.outbound.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateProductUseCaseHandler implements CreateProductUseCase {

  private final SequenceGenerator sequenceGenerator;
  private final ProductRepository productRepository;
  private final ProductEventPublisher productEventPublisher;

  @Value("${spring.application.name}")
  private String applicationName;

  @Override
  @Transactional
  public Output handle(Input input) throws Exception {
    // Insert product into DB
    ProductEntity productEntity = map(input);
    productEntity.setCode(sequenceGenerator.generate("product", "PRD", 7));
    productRepository.insert(productEntity);

    // Publish event
    publishEvent(productEntity);

    Integer newProductId = productEntity.getId();
    return new Output(newProductId);
  }

  private ProductEntity map(Input input) {
    // Product
    ProductEntity productEntity = ObjectMapperUtil.getInstance().map(input, ProductEntity.class);

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
    ProductDetailEntity productDetailEntity = ObjectMapperUtil.getInstance()
        .map(input.getDetail(), ProductDetailEntity.class);
    productEntity.setDetail(productDetailEntity);

    // Product images
    if (CollectionUtils.isNotEmpty(input.getImages())) {
      List<ProductImageEntity> productImageEntities = ObjectMapperUtil.getInstance()
          .mapList(input.getImages(), ProductImageEntity.class);
      productEntity.setImages(productImageEntities);
    }

    return productEntity;
  }

  private void publishEvent(ProductEntity productEntity) {
    ProductCreatedEvent event = new ProductCreatedEvent(applicationName);
    ObjectMapperUtil.getInstance().merge(productEntity, event);
    productEventPublisher.publish(event);
  }
}
