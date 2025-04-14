package org.atlas.edge.auth.springsecurityjwt.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogoutRequest {

  @NotBlank
  private String accessToken;

  @NotBlank
  private String refreshToken;
}
