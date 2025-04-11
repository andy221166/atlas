package org.atlas.framework.jwt;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import org.atlas.domain.user.shared.enums.Role;

@Data
@Builder
public class EncodeJwtInput {

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
  // RSA keys
  private RSAPublicKey rsaPublicKey;
  private RSAPrivateKey rsaPrivateKey;
  // Custom claims
  private Integer userId;
  private Role userRole;
}
