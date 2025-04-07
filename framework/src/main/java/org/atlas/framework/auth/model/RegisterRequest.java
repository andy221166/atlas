package org.atlas.framework.auth.model;

import java.util.Map;
import lombok.Data;

@Data
public class RegisterRequest {

  private String username;
  private String plainPassword;
  private Map<String, Object> claims;
}
