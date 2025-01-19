package org.atlas.platform.orm.mybatis.product.adapter.search;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.model.PageDto;
import org.atlas.platform.orm.mybatis.product.mapper.ProductMapper;
import org.atlas.service.product.contract.query.SearchProductQuery;
import org.atlas.service.product.contract.search.ProductSearchService;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProductSearchServiceAdapter implements ProductSearchService {

  private final ProductMapper productMapper;

  @Override
  @Transactional(readOnly = true)
  public PageDto<Product> search(SearchProductQuery query) {
    long totalCount = productMapper.count(query);
    if (totalCount == 0L) {
      return PageDto.empty();
    }

    List<Product> products = productMapper.find(query);

    return PageDto.of(products, totalCount);
  }
}
