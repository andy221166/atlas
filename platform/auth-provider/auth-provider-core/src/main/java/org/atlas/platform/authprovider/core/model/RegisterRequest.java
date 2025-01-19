package org.atlas.platform.authprovider.core.model;

import lombok.Data;
import org.atlas.commons.enums.Role;

@Data
public class RegisterRequest {

  private String username;
  private String password;
  private Role role;
}
