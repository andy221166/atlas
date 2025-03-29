package org.atlas.service.product.application.admin;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.enums.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.port.inbound.admin.GetProductUseCase;
import org.atlas.service.product.port.outbound.repository.ProductRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("adminGetProductUseCaseHandler")
@RequiredArgsConstructor
public class GetProductUseCaseHandler implements GetProductUseCase {

  private final ProductRepositoryPort productRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public GetProductOutput handle(GetProductInput input) throws Exception {
    ProductEntity productEntity = productRepositoryPort.findById(input.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));
    return ObjectMapperUtil.getInstance().map(productEntity, GetProductOutput.class);
  }
}
