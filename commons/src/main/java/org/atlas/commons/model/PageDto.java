package org.atlas.commons.model;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PageDto<T> {

  private List<T> records;
  private long totalCount;

  public static <T> PageDto<T> empty() {
    return new PageDto<>(Collections.emptyList(), 0);
  }

  public static <T> PageDto<T> of(List<T> records, long totalCount) {
    return new PageDto<>(records, totalCount);
  }
}
