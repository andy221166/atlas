package org.atlas.service.user.adapter.api.server.rest.internal.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.enums.Role;

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
