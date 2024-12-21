package org.atlas.platform.orm.jpa.report.projection;

import java.math.BigDecimal;

public interface UserDtoProjection {

  Integer getId();

  String getFirstName();

  String getLastName();

  BigDecimal getTotalAmount();
}