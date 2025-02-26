package org.atlas.platform.commons.paging;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.index.qual.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingRequest {

  @Positive
  @Min(0)
  private Integer page;

  @Positive
  @Min(0)
  private Integer size;

  private String sortBy;

  private SortOrder sortOrder = SortOrder.ASC;

  public static PagingRequest of(Integer page, Integer size) {
    PagingRequest instance = new PagingRequest();
    instance.page = page;
    instance.size = size;
    return instance;
  }

  public Integer getLimit() {
    return size;
  }

  public Integer getOffset() {
    return page * size;
  }

  public boolean hasPaging() {
    return page > 0 && size > 0;
  }

  public boolean hasSort() {
    return StringUtils.isNotBlank(sortBy);
  }

  public boolean isSortAscending() {
    return SortOrder.ASC.equals(sortOrder);
  }

  public boolean isSortDescending() {
    return !isSortAscending();
  }

  public enum SortOrder {

    ASC, DESC;
  }
}
