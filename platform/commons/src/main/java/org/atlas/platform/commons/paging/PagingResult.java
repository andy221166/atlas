package org.atlas.platform.commons.paging;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingResult<T> {

  private List<T> results;
  private long totalCount;

  public static <T> PagingResult<T> empty() {
    return new PagingResult<>(Collections.emptyList(), 0);
  }

  public static <T> PagingResult<T> of(List<T> results, long totalCount) {
    return new PagingResult<>(results, totalCount);
  }
}
