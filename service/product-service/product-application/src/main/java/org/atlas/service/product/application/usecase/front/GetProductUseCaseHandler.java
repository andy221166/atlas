package org.atlas.service.product.application.usecase.front;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.commons.enums.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.service.product.domain.entity.CategoryEntity;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.domain.entity.ProductImageEntity;
import org.atlas.service.product.port.inbound.usecase.front.GetProductUseCase;
import org.atlas.service.product.port.inbound.usecase.front.GetProductUseCase.Output.Product;
import org.atlas.service.product.port.outbound.repository.ProductRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetProductUseCaseHandler implements GetProductUseCase {

  private final ProductRepositoryPort productRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public Output handle(Input input) throws Exception {
    ProductEntity productEntity = productRepositoryPort.findById(input.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));
    Product product = map(productEntity);
    return new Output(product);
  }

  private Product map(ProductEntity productEntity) {
    Product product = new Product();
    product.setId(productEntity.getId());
    product.setName(productEntity.getName());
    product.setPrice(productEntity.getPrice());
    product.setBrand(productEntity.getBrand() != null ?
        productEntity.getBrand().getName() : null);
    product.setDescription(productEntity.getDetail() != null ?
        productEntity.getDetail().getDescription() : null);
    if (CollectionUtils.isNotEmpty(productEntity.getImages())) {
      product.setImageUrls(productEntity.getImages()
          .stream()
          .map(ProductImageEntity::getImageUrl)
          .toList());
    }
    if (CollectionUtils.isNotEmpty(productEntity.getCategories())) {
      product.setCategories(productEntity.getCategories()
          .stream()
          .map(CategoryEntity::getName)
          .toList());
    }
    return product;
  }
}
