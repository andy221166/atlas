package org.atlas.platform.api.server.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.commons.paging.Pagination;
import org.atlas.platform.commons.paging.PagingResult;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagingResponse<T> extends Response<List<T>> {

  private Pagination pagination;

  public static <T> PagingResponse<T> success(PagingResult<T> pagingResult) {
    PagingResponse<T> instance = new PagingResponse<>();
    instance.setSuccess(true);
    instance.setData(pagingResult.getData());
    instance.setPagination(pagingResult.getPagination());
    return instance;
  }
}
