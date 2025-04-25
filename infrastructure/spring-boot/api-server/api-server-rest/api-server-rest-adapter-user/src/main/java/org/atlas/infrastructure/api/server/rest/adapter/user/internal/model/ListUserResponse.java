package org.atlas.infrastructure.api.server.rest.adapter.user.internal.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Response object containing a list of users.")
public class ListUserResponse {

  @Schema(description = "List of users.", example = "[{\"id\": 1, \"username\": \"john_doe\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"phoneNumber\": \"+1234567890\", \"role\": \"USER\"}]")
  private List<User> users;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "User object containing user details.")
  public static class User {

    @Schema(description = "Unique identifier for the user.", example = "1")
    private Integer id;

    @Schema(description = "Username of the user.", example = "john_doe")
    private String username;

    @Schema(description = "First name of the user.", example = "John")
    private String firstName;

    @Schema(description = "Last name of the user.", example = "Doe")
    private String lastName;

    @Schema(description = "Email address of the user.", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Phone number of the user.", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "Role of the user in the system.", example = "USER")
    private Role role;
  }
}
