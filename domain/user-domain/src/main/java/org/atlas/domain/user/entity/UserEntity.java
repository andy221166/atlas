package org.atlas.domain.user.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.framework.domain.entity.DomainEntity;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class UserEntity extends DomainEntity {

  @EqualsAndHashCode.Include
  private Integer id;

  private String username;

  private String firstName;

  private String lastName;

  private String email;

  private String phoneNumber;

  private Role role;
}
