package org.atlas.platform.commons.paging;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingResult<T> {

  protected List<T> results;
  protected Pagination pagination;

  public boolean checkEmpty() {
    return pagination.getTotalRecords() == 0L;
  }

  public static <T> PagingResult<T> empty() {
    return new PagingResult<>(Collections.emptyList(), Pagination.empty());
  }

  public static <T> PagingResult<T> of(List<T> data, long totalRecords,
      PagingRequest pagingRequest) {
    return new PagingResult<>(data, Pagination.of(totalRecords, pagingRequest));
  }

  public <U> PagingResult<U> map(Function<? super T, ? extends U> mapper) {
    List<U> mappedResults = results.stream()
        .map(mapper)
        .collect(Collectors.toList());
    return new PagingResult<>(mappedResults, pagination);
  }
}
