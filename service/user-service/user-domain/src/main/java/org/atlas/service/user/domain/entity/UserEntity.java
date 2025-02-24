package org.atlas.service.user.domain.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.commons.enums.Role;
import org.atlas.platform.commons.model.AuditableEntity;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class UserEntity extends AuditableEntity implements Serializable {

  @EqualsAndHashCode.Include
  private Integer id;

  private String username;

  private String password;

  private String firstName;

  private String lastName;

  private String phoneNumber;

  private Role role;
}
