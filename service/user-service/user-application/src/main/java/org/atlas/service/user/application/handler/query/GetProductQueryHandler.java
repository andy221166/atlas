package org.atlas.service.user.application.handler.query;

import lombok.RequiredArgsConstructor;
import org.atlas.commons.exception.AppError;
import org.atlas.commons.exception.BusinessException;
import org.atlas.platform.cqrs.handler.QueryHandler;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.contract.model.ProductDto;
import org.atlas.service.product.contract.query.GetProductQuery;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetProductQueryHandler implements QueryHandler<GetProductQuery, ProductDto> {

  private final ProductRepository productRepository;

  @Override
  @Transactional(readOnly = true)
  public ProductDto handle(GetProductQuery query) throws Exception {
    return productRepository.findById(query.getId())
        .map(product -> ObjectMapperUtil.getInstance().map(product, ProductDto.class))
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));
  }
}
