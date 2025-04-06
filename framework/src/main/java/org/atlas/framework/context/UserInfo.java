package org.atlas.framework.context;

import lombok.Data;
import org.atlas.domain.user.shared.enums.Role;

@Data
public class UserInfo {

  private Integer userId;
  private Role role;

  public boolean isAdmin() {
    return Role.ADMIN.equals(role);
  }

  public boolean isUser() {
    return Role.USER.equals(role);
  }
}
