package org.atlas.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingQuery {

  private static final String DESCENDING_SIGN = "-";

  private Integer page;
  private Integer size;
  private String sort;

  public boolean hasPaging() {
    return page != null && size != null;
  }

  public Integer getLimit() {
    return size;
  }

  public Integer getOffset() {
    return page * size;
  }

  public boolean hasSort() {
    return StringUtils.isNotBlank(sort);
  }

  public String getSort() {
    if (!hasSort()) {
      return null;
    }
    if (sort.startsWith(DESCENDING_SIGN)) {
      return sort.substring(DESCENDING_SIGN.length());
    }
    return sort;
  }

  public boolean isSortAscending() {
    return !isSortDescending();
  }

  public boolean isSortDescending() {
    return sort.startsWith(DESCENDING_SIGN);
  }
}
