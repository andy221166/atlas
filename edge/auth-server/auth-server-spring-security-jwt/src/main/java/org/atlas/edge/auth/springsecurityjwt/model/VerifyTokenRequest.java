package org.atlas.edge.auth.springsecurityjwt.model;

import lombok.Data;

@Data
public class VerifyTokenRequest {

  private String accessToken;
}
