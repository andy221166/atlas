package org.atlas.platform.auth.jwt.core;

import java.util.Date;
import lombok.Builder;
import lombok.Data;
import org.atlas.commons.enums.Role;

@Data
@Builder
public class JwtData {

  // 'jti' claim
  private String jwtId;
  // 'iss' claim
  private String issuer;
  // 'iat' claim
  private Date issuedAt;
  // 'sub' claim
  private String subject;
  // 'aud' claim
  private String audience;
  // 'exp' claim
  private Date expiredAt;
  // Custom claims
  private Integer userId;
  private Role userRole;
}
