package org.atlas.commons.util.paging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
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

  @JsonIgnore
  public boolean isEmpty() {
    return totalCount == 0L;
  }

  public <U> PageDto<U> map(Function<? super T, ? extends U> mapper) {
    List<U> newRecords = this.records.stream()
        .map(mapper)
        .collect(Collectors.toList());
    return new PageDto<>(newRecords, this.totalCount);
  }
}
