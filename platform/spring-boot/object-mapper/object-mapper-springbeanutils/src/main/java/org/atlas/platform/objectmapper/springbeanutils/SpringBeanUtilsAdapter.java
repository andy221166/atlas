package org.atlas.platform.objectmapper.springbeanutils;

import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
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
    if (CollectionUtils.isEmpty(source)) {
      return List.of();
    }
    return source.stream()
        .filter(Objects::nonNull)
        .map(it -> map(it, destinationType))
        .toList();
  }

  @Override
  public void merge(Object source, Object destination) {
    if (source == null || destination == null) {
      throw new IllegalArgumentException("Source and Destination objects cannot be null.");
    }
    BeanUtils.copyProperties(source, destination);
  }
}
