package org.atlas.service.product.port.outbound.search;

import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.service.product.domain.entity.ProductEntity;

public interface SearchService {

  PagingResult<ProductEntity> search(SearchProductCriteria criteria, PagingRequest pagingRequest);
}
