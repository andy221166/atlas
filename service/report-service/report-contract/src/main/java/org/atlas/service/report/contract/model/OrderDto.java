package org.atlas.service.report.contract.model;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class OrderDto {

  private Integer id;
  private Integer userId;
  private String firstName;
  private String lastName;
  private BigDecimal amount;
  private Date createdAt;
}
