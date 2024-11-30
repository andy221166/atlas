package org.atlas.platform.persistence.jpa.report.projection;

import java.math.BigDecimal;

public interface UserDtoProjection {

  Integer getId();

  String getFirstName();

  String getLastName();

  BigDecimal getTotalAmount();
}