package org.atlas.framework.jwt.auth0;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.framework.auth.CustomClaim;
import org.atlas.framework.util.UUIDGenerator;
import org.atlas.framework.jwt.JwtData;
import org.atlas.framework.jwt.JwtService;

public class Auth0JwtService extends JwtService {

  @Override
  public String generateJwt(JwtData jwtData) {
    return JWT.create()
        .withJWTId(UUIDGenerator.generate())
        .withIssuer(jwtData.getIssuer())
        .withIssuedAt(jwtData.getIssuedAt())
        .withSubject(jwtData.getSubject())
        .withAudience(jwtData.getAudience())
        .withExpiresAt(jwtData.getExpiredAt())
        .withClaim(CustomClaim.USER_ID.value(), jwtData.getUserId())
        .withClaim(CustomClaim.USER_ROLE.value(), jwtData.getUserRole().name())
        .sign(Algorithm.RSA256(RSA_PUBLIC_KEY, RSA_PRIVATE_KEY));
  }

  @Override
  public JwtData decodeJwt(String jwt, String issuer) {
    if (jwt.startsWith(JWT_PREFIX)) {
      jwt = jwt.substring(JWT_PREFIX.length());
    }
    DecodedJWT decodedJWT;
    try {
      decodedJWT = JWT.require(Algorithm.RSA256(RSA_PUBLIC_KEY))
          .withIssuer(issuer)
          .build()
          .verify(jwt);
    } catch (JWTVerificationException e) {
      throw new RuntimeException(e);
    }
    return JwtData.builder()
        .jwtId(decodedJWT.getId())
        .issuer(decodedJWT.getIssuer())
        .issuedAt(decodedJWT.getIssuedAt())
        .subject(decodedJWT.getSubject())
        .audience(decodedJWT.getAudience().get(0))
        .expiredAt(decodedJWT.getExpiresAt())
        .userId(decodedJWT.getClaim(CustomClaim.USER_ID.value()).asInt())
        .userRole(Role.valueOf(decodedJWT.getClaim(CustomClaim.USER_ROLE.value()).asString()))
        .build();
  }
}
