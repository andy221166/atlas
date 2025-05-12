package org.atlas.domain.product.usecase.admin.handler;

import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.framework.domain.exception.DomainException;
import org.atlas.framework.error.AppError;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class AdminGetProductUseCaseHandler implements UseCaseHandler<Integer, ProductEntity> {

  private final ProductRepository productRepository;

  @Override
  public ProductEntity handle(Integer productId) throws Exception {
    return productRepository.findById(productId)
        .orElseThrow(() -> new DomainException(AppError.PRODUCT_NOT_FOUND));
  }
}
