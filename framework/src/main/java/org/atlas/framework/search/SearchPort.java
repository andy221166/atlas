package org.atlas.framework.search;

import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;

public interface SearchPort {

  PagingResult<SearchResponse> search(SearchCriteria criteria, PagingRequest pagingRequest);
}
