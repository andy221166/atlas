package org.atlas.service.user.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.commons.enums.Role;
import org.atlas.commons.model.Auditable;

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
  private Date birthday;
  private Boolean gender;
  private BigDecimal balance;
  private Role role;

  // One-To-One
  private Preference preference;
  // One-To-Many
  private List<Address> addresses;
  // Many-To-One
  private Organization organization;
  // Many-To-Many
  private List<Group> groups;
}
