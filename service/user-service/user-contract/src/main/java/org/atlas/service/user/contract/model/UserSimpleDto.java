package org.atlas.service.user.contract.model;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import org.atlas.commons.enums.Role;

@Data
public class UserSimpleDto {

  private Integer id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private Date birthday;
  private Boolean gender;
  private BigDecimal balance;
  private Role role;
}
