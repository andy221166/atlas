package org.atlas.platform.objectmapper;

import java.util.List;
import org.atlas.platform.commons.paging.PagingResult;

public interface ObjectMapper {

  <D> D map(Object source, Class<D> destinationType);

  <D> List<D> mapList(List<?> source, Class<D> destinationType);

  default <D> PagingResult<D> mapPage(PagingResult<?> source, Class<D> destinationType) {
    if (source.isEmpty()) {
      return PagingResult.empty();
    }
    return new PagingResult<>(mapList(source.getData(), destinationType), source.getPagination());
  }

  void merge(Object source, Object destination);
}
