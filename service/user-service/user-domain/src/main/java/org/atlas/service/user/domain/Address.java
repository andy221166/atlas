package org.atlas.service.user.domain;

import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.atlas.commons.model.Auditable;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Address extends Auditable implements Serializable {

  private Integer id;
  private String street;
  private String district;
  private String city;
  private String country;
  private String zipCode;

  public String getFullAddress() {
    return Stream.of(street, district, city, country, zipCode)
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.joining(", "));
  }
}
