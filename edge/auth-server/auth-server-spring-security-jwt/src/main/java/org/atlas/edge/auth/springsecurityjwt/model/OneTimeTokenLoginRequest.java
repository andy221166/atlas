package org.atlas.edge.auth.springsecurityjwt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request object for user login containing username and one-time token.")
public class OneTimeTokenLoginRequest {

  @NotBlank
  @Schema(description = "Username, email, of phone number of the user attempting to log in.", example = "john_doe")
  private String identifier;

  @NotBlank
  @Schema(description = "The given one-time token.", example = "123456")
  private String token;
}
