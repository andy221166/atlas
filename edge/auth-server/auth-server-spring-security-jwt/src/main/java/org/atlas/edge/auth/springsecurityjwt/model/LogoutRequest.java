package org.atlas.edge.auth.springsecurityjwt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request object for user logout containing access and refresh tokens.")
public class LogoutRequest {

  @NotBlank
  @Schema(description = "Access token to be invalidated.", example = "eyJhbGciOiJIUzI1...")
  private String accessToken;

  @NotBlank
  @Schema(description = "Refresh token to be invalidated.", example = "dGhpc0lzUmVmcmVzaFRva2Vu")
  private String refreshToken;
}
