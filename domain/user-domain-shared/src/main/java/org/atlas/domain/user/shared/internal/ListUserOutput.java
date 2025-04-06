package org.atlas.domain.user.shared.internal;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.domain.user.shared.enums.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListUserOutput {

  private List<User> users;

  @Data
  public static class User {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;
  }
}
