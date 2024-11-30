package org.atlas.service.product.contract.search;

import org.atlas.commons.util.paging.PageDto;
import org.atlas.service.product.contract.query.SearchProductQuery;
import org.atlas.service.product.domain.Product;

public interface ProductSearchService {

  PageDto<Product> search(SearchProductQuery command);
}
