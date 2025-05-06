package org.atlas.framework.jwt.auth0;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.framework.jwt.DecodeJwtInput;
import org.atlas.framework.jwt.EncodeJwtInput;
import org.atlas.framework.jwt.InvalidJwtException;
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
        .withExpiresAt(input.getJwt().getExpiresAt());

    // Custom claims
    if (input.getJwt().getUserRole() != null) {
      builder.withClaim(CustomClaim.USER_ROLE.getClaim(), input.getJwt().getUserRole().name());
    }
    if (StringUtils.isNotBlank(input.getJwt().getSessionId())) {
      builder.withClaim(CustomClaim.SESSION_ID.getClaim(), input.getJwt().getSessionId());
    }

    return builder.sign(Algorithm.RSA256(input.getRsaPublicKey(), input.getRsaPrivateKey()));
  }

  @Override
  public Jwt decodeJwt(DecodeJwtInput input) throws InvalidJwtException {
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
      throw new InvalidJwtException(e);
    }

    Jwt.JwtBuilder builder = Jwt.builder()
        .jwtId(decodedJWT.getId())
        .issuer(decodedJWT.getIssuer())
        .issuedAt(decodedJWT.getIssuedAt())
        .subject(decodedJWT.getSubject())
        .audience(decodedJWT.getAudience().get(0))
        .expiresAt(decodedJWT.getExpiresAt());

    // Custom claims
    String userRole = decodedJWT.getClaim(CustomClaim.USER_ROLE.getClaim()).asString();
    if (userRole != null) {
      builder.userRole(Role.valueOf(userRole));
    }
    String sessionId = decodedJWT.getClaim(CustomClaim.SESSION_ID.getClaim()).asString();
    if (sessionId != null) {
      builder.sessionId(sessionId);
    }

    return builder.build();
  }
}
