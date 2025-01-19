package org.atlas.service.user.contract.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.atlas.commons.enums.Role;

@Data
public class UserDto {

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
  private String language;
  private String timezone;
  private String organization;
  private List<String> groups;
  private List<String> addresses;
}
