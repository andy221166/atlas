package org.atlas.infrastructure.api.server.rest.adapter.user.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request object for user login containing username and password.")
public class LoginRequest {

  @NotBlank
  @Schema(description = "Username of the user attempting to log in.", example = "john_doe")
  private String username;

  @NotBlank
  @Schema(description = "Password of the user attempting to log in.", example = "P@ssw0rd")
  private String password;
}
