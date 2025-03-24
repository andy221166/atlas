package org.atlas.platform.api.server.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.commons.paging.PagingResult;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagingResponse<T> extends Response<List<T>> {

  public static <T> PagingResponse<T> success(PagingResult<T> pagingResult) {
    PagingResponse<T> instance = new PagingResponse<>();
    instance.setSuccess(true);
    instance.setData(pagingResult.getResults());
    instance.setMetadata(Map.of("totalCount", pagingResult.getTotalCount()));
    return instance;
  }
}
