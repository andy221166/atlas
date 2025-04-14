package org.atlas.framework.jwt.auth0;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.framework.jwt.DecodeJwtInput;
import org.atlas.framework.jwt.EncodeJwtInput;
import org.atlas.framework.jwt.Jwt;
import org.atlas.framework.jwt.JwtService;
import org.atlas.framework.security.enums.CustomClaim;
import org.atlas.framework.util.UUIDGenerator;

public class Auth0JwtService implements JwtService {

  @Override
  public String encodeJwt(EncodeJwtInput input) {
    JWTCreator.Builder builder = JWT.create()
        .withJWTId(UUIDGenerator.generate())
        .withIssuer(input.getJwt().getIssuer())
        .withIssuedAt(input.getJwt().getIssuedAt())
        .withSubject(input.getJwt().getSubject())
        .withAudience(input.getJwt().getAudience())
        .withExpiresAt(input.getJwt().getExpiredAt());

    // Custom claims
    if (input.getJwt().getUserId() != null) {
      builder.withClaim(CustomClaim.USER_ID.getClaim(), input.getJwt().getUserId());
    }
    if (input.getJwt().getUserRole() != null) {
      builder.withClaim(CustomClaim.USER_ROLE.getClaim(), input.getJwt().getUserRole().name());
    }

    return builder.sign(Algorithm.RSA256(input.getRsaPublicKey(), input.getRsaPrivateKey()));
  }

  @Override
  public Jwt decodeJwt(DecodeJwtInput input) {
    String jwt = input.getJwt();

    // Strip prefix
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

    Jwt.JwtBuilder builder = Jwt.builder()
        .jwtId(decodedJWT.getId())
        .issuer(decodedJWT.getIssuer())
        .issuedAt(decodedJWT.getIssuedAt())
        .subject(decodedJWT.getSubject())
        .audience(decodedJWT.getAudience().get(0))
        .expiredAt(decodedJWT.getExpiresAt());

    // Custom claims
    Integer userId = decodedJWT.getClaim(CustomClaim.USER_ID.getClaim()).asInt();
    if (userId != null) {
      builder.userId(userId);
    }
    String userRole = decodedJWT.getClaim(CustomClaim.USER_ROLE.getClaim()).asString();
    if (userRole != null) {
      builder.userRole(Role.valueOf(userRole));
    }

    return builder.build();
  }
}
