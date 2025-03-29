package org.atlas.service.user.adapter.api.server.rest.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

  private String accessToken;
  private String refreshToken;
}
