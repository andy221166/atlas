package org.atlas.framework.jwt.auth0;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.framework.security.enums.CustomClaim;
import org.atlas.framework.jwt.DecodeJwtInput;
import org.atlas.framework.jwt.DecodeJwtOutput;
import org.atlas.framework.jwt.EncodeJwtInput;
import org.atlas.framework.jwt.JwtService;
import org.atlas.framework.util.UUIDGenerator;

public class Auth0JwtService implements JwtService {

  @Override
  public String encodeJwt(EncodeJwtInput encodeJwtInput) {
    return JWT.create()
        .withJWTId(UUIDGenerator.generate())
        .withIssuer(encodeJwtInput.getIssuer())
        .withIssuedAt(encodeJwtInput.getIssuedAt())
        .withSubject(encodeJwtInput.getSubject())
        .withAudience(encodeJwtInput.getAudience())
        .withExpiresAt(encodeJwtInput.getExpiredAt())
        .withClaim(CustomClaim.USER_ID.getClaim(), encodeJwtInput.getUserId())
        .withClaim(CustomClaim.USER_ROLE.getClaim(), encodeJwtInput.getUserRole().name())
        .sign(
            Algorithm.RSA256(encodeJwtInput.getRsaPublicKey(), encodeJwtInput.getRsaPrivateKey()));
  }

  @Override
  public DecodeJwtOutput decodeJwt(DecodeJwtInput input) {
    String jwt = input.getJwt();
    if (input.getJwt().startsWith(JWT_PREFIX)) {
      jwt = input.getJwt().substring(JWT_PREFIX.length());
    }
    DecodedJWT decodedJWT;
    try {
      decodedJWT = JWT.require(Algorithm.RSA256(input.getRsaPublicKey()))
          .withIssuer(input.getIssuer())
          .build()
          .verify(jwt);
    } catch (JWTVerificationException e) {
      throw new RuntimeException(e);
    }
    return DecodeJwtOutput.builder()
        .jwtId(decodedJWT.getId())
        .issuer(decodedJWT.getIssuer())
        .issuedAt(decodedJWT.getIssuedAt())
        .subject(decodedJWT.getSubject())
        .audience(decodedJWT.getAudience().get(0))
        .expiredAt(decodedJWT.getExpiresAt())
        .userId(decodedJWT.getClaim(CustomClaim.USER_ID.getClaim()).asInt())
        .userRole(Role.valueOf(decodedJWT.getClaim(CustomClaim.USER_ROLE.getClaim()).asString()))
        .build();
  }
}
