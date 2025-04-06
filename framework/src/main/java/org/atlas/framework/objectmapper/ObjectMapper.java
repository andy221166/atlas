package org.atlas.framework.objectmapper;

import java.util.List;
import org.atlas.framework.paging.PagingResult;

public interface ObjectMapper {

  <D> D map(Object source, Class<D> destinationType);

  <D> List<D> mapList(List<?> source, Class<D> destinationType);

  default <D> PagingResult<D> mapPage(PagingResult<?> source, Class<D> destinationType) {
    if (source.checkEmpty()) {
      return PagingResult.empty();
    }
    return new PagingResult<>(mapList(source.getResults(), destinationType), source.getPagination());
  }

  void merge(Object source, Object destination);
}
