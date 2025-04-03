package org.atlas.service.product.port.outbound.search;

import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.domain.product.entity.ProductEntity;

public interface SearchPort {

  PagingResult<ProductEntity> search(SearchCriteria criteria, PagingRequest pagingRequest);
}
