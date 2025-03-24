package org.atlas.service.product.application.usecase.admin;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.enums.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.port.inbound.usecase.admin.GetProductUseCase;
import org.atlas.service.product.port.inbound.usecase.admin.GetProductUseCase.Output.Product;
import org.atlas.service.product.port.outbound.repository.ProductRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("adminGetProductUseCaseHandler")
@RequiredArgsConstructor
public class GetProductUseCaseHandler implements GetProductUseCase {

  private final ProductRepositoryPort productRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public Output handle(Input input) throws Exception {
    ProductEntity productEntity = productRepositoryPort.findById(input.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));
    Product product = ObjectMapperUtil.getInstance().map(productEntity, Product.class);
    return new Output(product);
  }
}
