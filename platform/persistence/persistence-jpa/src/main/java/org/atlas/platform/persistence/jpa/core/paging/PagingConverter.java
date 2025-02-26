package org.atlas.platform.persistence.jpa.core.paging;

import lombok.experimental.UtilityClass;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@UtilityClass
public class PagingConverter {

  public static Pageable convert(PagingRequest pagingRequest) {
    if (pagingRequest.hasSort()) {
      Sort.Direction sortDirection = pagingRequest.isSortAscending() ?
          Sort.Direction.ASC : Sort.Direction.DESC;
      Sort sort = Sort.by(sortDirection, pagingRequest.getSortBy());
      return org.springframework.data.domain.PageRequest.of(
          pagingRequest.getPage(), pagingRequest.getSize(), sort);
    } else {
      return org.springframework.data.domain.PageRequest.of(
          pagingRequest.getPage(), pagingRequest.getSize());
    }
  }

  public static <T> PagingResult<T> convert(org.springframework.data.domain.Page<T> springPage) {
    if (springPage.isEmpty()) {
      return PagingResult.empty();
    }
    return PagingResult.of(springPage.getContent(), springPage.getTotalElements());
  }
}
