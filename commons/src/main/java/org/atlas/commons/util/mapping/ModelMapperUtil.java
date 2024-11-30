package org.atlas.commons.util.mapping;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModelMapperUtil {

  private static final ModelMapper MAPPER;

  static {
    MAPPER = new ModelMapper();
  }

  public static <D> D map(Object source, Class<D> destinationType) {
    return MAPPER.map(source, destinationType);
  }

  /**
   * Suppose you have a source object and a destination object, and you want to map properties from
   * the source to the destination. By default, ModelMapper will attempt to map all properties, even
   * those that are null in the source object. This configuration setting ensures that only non-null
   * properties from the source object are mapped to the destination object, preventing null values
   * from overwriting existing values in the destination object.
   */
  public static void merge(Object source, Object destination) {
    MAPPER.getConfiguration().setPropertyCondition(context -> context.getSource() != null);
    MAPPER.map(source, destination);
  }
}
