package org.atlas.platform.objectmapper;

import java.util.List;
import org.atlas.platform.commons.paging.PageResult;

public interface ObjectMapper {

  <D> D map(Object source, Class<D> destinationType);
  <D> List<D> mapList(List<?> source, Class<D> destinationType);
  <D> PageResult<D> mapPage(PageResult<?> source, Class<D> destinationType);
  void merge(Object source, Object destination);
}
