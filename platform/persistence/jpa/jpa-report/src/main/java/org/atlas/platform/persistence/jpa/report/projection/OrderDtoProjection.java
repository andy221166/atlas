package org.atlas.platform.persistence.jpa.report.projection;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

public interface OrderDtoProjection {

  Integer getId();

  Integer getUserId();

  String getFirstName();

  String getLastName();

  BigDecimal getAmount();

  Date getCreatedAt();
}
