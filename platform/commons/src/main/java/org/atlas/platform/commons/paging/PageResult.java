package org.atlas.platform.commons.paging;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

  private List<T> results;
  private long totalCount;

  public static <T> PageResult<T> empty() {
    return new PageResult<>(Collections.emptyList(), 0);
  }

  public static <T> PageResult<T> of(List<T> results, long totalCount) {
    return new PageResult<>(results, totalCount);
  }
}
