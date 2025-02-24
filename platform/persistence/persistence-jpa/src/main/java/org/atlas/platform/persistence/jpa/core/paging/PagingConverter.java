package org.atlas.platform.persistence.jpa.core.paging;

import lombok.experimental.UtilityClass;
import org.atlas.platform.commons.paging.PageResult;
import org.atlas.platform.commons.paging.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@UtilityClass
public class PagingConverter {

  public static Pageable convert(PageRequest pageRequest) {
    if (pageRequest.hasSort()) {
      Sort.Direction sortDirection = pageRequest.isSortAscending() ?
          Sort.Direction.ASC : Sort.Direction.DESC;
      Sort sort = Sort.by(sortDirection, pageRequest.getSortBy());
      return org.springframework.data.domain.PageRequest.of(
          pageRequest.getPage(), pageRequest.getSize(), sort);
    } else {
      return org.springframework.data.domain.PageRequest.of(
          pageRequest.getPage(), pageRequest.getSize());
    }
  }

  public static <T> PageResult<T> convert(org.springframework.data.domain.Page<T> springPage) {
    if (springPage.isEmpty()) {
      return PageResult.empty();
    }
    return PageResult.of(springPage.getContent(), springPage.getTotalElements());
  }
}
