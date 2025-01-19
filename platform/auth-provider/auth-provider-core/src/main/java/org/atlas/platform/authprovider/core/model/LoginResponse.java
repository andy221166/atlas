package org.atlas.platform.authprovider.core.model;

import lombok.Data;

@Data
public class LoginResponse {

  private String accessToken;
  private String refreshToken;
}
