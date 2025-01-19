package org.atlas.platform.authprovider.core.model;

import lombok.Data;

@Data
public class LogoutRequest {

  private String accessToken;
}
