package org.atlas.commons.context;

import lombok.Data;
import org.atlas.commons.enums.Role;

@Data
public class CurrentUser {

  private Integer userId;
  private Role role;

  public boolean isAdmin() {
    return Role.ADMIN.equals(role);
  }

  public boolean isCustomer() {
    return Role.CUSTOMER.equals(role);
  }
}
