package org.atlas.service.catalog.port.outbound.search;

import org.atlas.platform.commons.paging.PageResult;
import org.atlas.service.catalog.domain.entity.ProductEntity;

public interface SearchService {

  PageResult<ProductEntity> search(SearchProductCriteria criteria);
}
