package org.atlas.service.user.application.handler.query;

import lombok.RequiredArgsConstructor;
import org.atlas.commons.model.PageDto;
import org.atlas.platform.cqrs.handler.QueryHandler;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.contract.model.ProductDto;
import org.atlas.service.product.contract.query.ListProductQuery;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListProductQueryHandler implements
    QueryHandler<ListProductQuery, PageDto<ProductDto>> {

  private final ProductRepository productRepository;

  @Override
  @Transactional(readOnly = true)
  public PageDto<ProductDto> handle(ListProductQuery query) throws Exception {
    PageDto<Product> productPage = productRepository.findAll(query);
    return ObjectMapperUtil.getInstance().mapPage(productPage, ProductDto.class);
  }
}
