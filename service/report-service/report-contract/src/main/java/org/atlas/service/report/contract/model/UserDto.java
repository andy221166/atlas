package org.atlas.service.report.contract.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class UserDto {

  private Integer id;
  private String firstName;
  private String lastName;
  private BigDecimal totalAmount;
}