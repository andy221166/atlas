package org.atlas.service.product.application.front;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.commons.enums.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.domain.product.entity.CategoryEntity;
import org.atlas.domain.product.entity.ProductAttributeEntity;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.port.inbound.product.front.GetProductUseCase;
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
  public GetProductOutput handle(GetProductInput input) throws Exception {
    ProductEntity productEntity = productRepositoryPort.findById(input.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));
    return map(productEntity);
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
}
