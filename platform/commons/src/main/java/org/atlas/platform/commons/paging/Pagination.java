package org.atlas.platform.commons.paging;

import lombok.Data;

@Data
public class Pagination {

  private int currentPage;
  private int pageSize;
  private int totalPages;
  private long totalRecords;

  public static Pagination empty() {
    return new Pagination();
  }

  public static Pagination of(long totalRecords, PagingRequest pagingRequest) {
    Pagination pagination = new Pagination();
    pagination.setCurrentPage(pagingRequest.getPage());
    pagination.setPageSize(pagingRequest.getSize());
    pagination.setTotalPages(
        (int) Math.ceil((double) totalRecords / pagination.getPageSize()));
    pagination.setTotalRecords(totalRecords);
    return pagination;
  }
}
