package org.atlas.service.auth.contract.model;

import lombok.Data;

@Data
public class LoginResultDto {

  private String accessToken;

  private String refreshToken;
}
