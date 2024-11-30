package org.atlas.service.user.contract.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProfileDto {

  private Integer id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private BigDecimal credit;
}
