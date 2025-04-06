package org.atlas.infrastructure.internalapi.user.rest.model;

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
public class ListUserResponse {

  private List<User> users;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
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
