package org.atlas.service.product.application.usecase.front;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.commons.enums.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.service.product.domain.entity.CategoryEntity;
import org.atlas.service.product.domain.entity.ProductAttributeEntity;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.port.inbound.usecase.front.GetProductUseCase;
import org.atlas.service.product.port.outbound.repository.ProductRepositoryPort;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("frontGetProductUseCaseHandler")
@RequiredArgsConstructor
public class GetProductUseCaseHandler implements GetProductUseCase {

  private final ProductRepositoryPort productRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  @Cacheable("products")
  public Output handle(Input input) throws Exception {
    ProductEntity productEntity = productRepositoryPort.findById(input.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));
    return map(productEntity);
  }

  private Output map(ProductEntity productEntity) {
    Output product = new Output();
    product.setId(productEntity.getId());
    product.setName(productEntity.getName());
    product.setPrice(productEntity.getPrice());
    product.setImageUrl(productEntity.getImageUrl());
    product.setDescription(productEntity.getDetail() != null ?
        productEntity.getDetail().getDescription() : null);
    if (CollectionUtils.isNotEmpty(productEntity.getAttributes())) {
      product.setAttributes(productEntity.getAttributes()
          .stream()
          .collect(Collectors.toMap(ProductAttributeEntity::getAttributeName,
              ProductAttributeEntity::getAttributeValue)));
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
}
