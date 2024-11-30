package org.atlas.service.user.domain;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.commons.model.Auditable;
import org.atlas.service.user.domain.shared.enums.Role;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class User extends Auditable implements Serializable {

  @EqualsAndHashCode.Include
  private Integer id;
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private Role role;
}
