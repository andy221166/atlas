package org.atlas.platform.objectmapper;

import java.util.List;
import org.atlas.platform.commons.paging.PagingResult;

public interface ObjectMapper {

  <D> D map(Object source, Class<D> destinationType);
  <D> List<D> mapList(List<?> source, Class<D> destinationType);
  <D> PagingResult<D> mapPage(PagingResult<?> source, Class<D> destinationType);
  void merge(Object source, Object destination);
}
