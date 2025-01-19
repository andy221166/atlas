package org.atlas.platform.objectmapper;

import java.util.List;
import org.atlas.commons.model.PageDto;

public interface ObjectMapper {

  <D> D map(Object source, Class<D> destinationType);
  <D> List<D> mapList(List<?> source, Class<D> destinationType);
  <D> PageDto<D> mapPage(PageDto<?> source, Class<D> destinationType);
  void merge(Object source, Object destination);
}
