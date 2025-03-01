package org.atlas.service.user.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.atlas.platform.commons.enums.Role;
import org.atlas.platform.commons.model.DomainEntity;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class UserEntity extends DomainEntity {

  @EqualsAndHashCode.Include
  private Integer id;

  private String username;

  private String password;

  private String firstName;

  private String lastName;

  private String phoneNumber;

  private Role role;
}
