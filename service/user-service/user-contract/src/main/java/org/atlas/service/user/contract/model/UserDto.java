package org.atlas.service.user.contract.model;

import lombok.Data;
import org.atlas.service.user.domain.shared.enums.Role;

@Data
public class UserDto {

  private Integer id;
  private String username;
  private Role role;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
}
