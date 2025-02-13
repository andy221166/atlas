package org.atlas.platform.objectmapper.springbeanutils;

import java.util.List;
import java.util.Objects;
import org.atlas.commons.model.PageDto;
import org.atlas.platform.objectmapper.ObjectMapper;
import org.springframework.beans.BeanUtils;

public class SpringBeanUtilsAdapter implements ObjectMapper {

  @Override
  public <D> D map(Object source, Class<D> destinationType) {
    if (source == null) {
      return null;
    }
    try {
      D destination = destinationType.getDeclaredConstructor().newInstance();
      BeanUtils.copyProperties(source, destination);
      return destination;
    } catch (Exception e) {
      throw new RuntimeException("Failed to map using Spring BeanUtils", e);
    }
  }

  @Override
  public <D> List<D> mapList(List<?> source, Class<D> destinationType) {
    if (source == null || source.isEmpty()) {
      return List.of();
    }
    return source.stream()
        .filter(Objects::nonNull)
        .map(it -> map(it, destinationType))
        .toList();
  }

  @Override
  public <D> PageDto<D> mapPage(PageDto<?> source, Class<D> destinationType) {
    if (source.getTotalCount() == 0L) {
      return PageDto.empty();
    }
    return PageDto.of(mapList(source.getRecords(), destinationType), source.getTotalCount());
  }

  @Override
  public void merge(Object source, Object destination) {
    if (source == null || destination == null) {
      throw new IllegalArgumentException("Source and Destination objects cannot be null.");
    }
    BeanUtils.copyProperties(source, destination);
  }
}
