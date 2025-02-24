package org.atlas.platform.commons.paging;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.index.qual.Positive;

@Data
public class PageRequest {

  @Positive
  @Min(1)
  private Integer page;

  @Positive
  @Min(1)
  private Integer size;

  private String sortBy;

  private SortOrder sortOrder = SortOrder.ASC;

  public Integer getLimit() {
    return size;
  }

  public Integer getOffset() {
    return page * size;
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
