package org.atlas.service.product.application.handler.query;

import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.platform.cqrs.handler.QueryHandler;
import org.atlas.service.product.application.mapper.ProductMapper;
import org.atlas.service.product.contract.model.ProductDto;
import org.atlas.service.product.contract.query.SearchProductQuery;
import org.atlas.service.product.contract.search.ProductSearchService;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchProductQueryHandler implements
    QueryHandler<SearchProductQuery, PageDto<ProductDto>> {

  private final ProductSearchService searchService;

  @Override
  public PageDto<ProductDto> handle(SearchProductQuery query) throws Exception {
    PageDto<Product> productPage = searchService.search(query);
    return productPage.map(ProductMapper::map);
  }
}
