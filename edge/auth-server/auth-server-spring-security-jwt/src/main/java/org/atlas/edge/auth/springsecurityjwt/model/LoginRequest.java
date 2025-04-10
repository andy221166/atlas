package org.atlas.edge.auth.springsecurityjwt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request object for user login containing username and password.")
public class LoginRequest {

  @NotBlank
  @Schema(description = "Username, email, of phone number of the user attempting to log in.", example = "john_doe")
  private String identifier;

  @NotBlank
  @Schema(description = "Password of the user attempting to log in.", example = "P@ssw0rd")
  private String password;
}
