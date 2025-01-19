package org.atlas.platform.authprovider.core.model;

import lombok.Data;

@Data
public class LoginRequest {

  private String username;
  private String password;
}
