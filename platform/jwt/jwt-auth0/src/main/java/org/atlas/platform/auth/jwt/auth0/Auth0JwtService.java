package org.atlas.platform.auth.jwt.auth0;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import org.atlas.commons.enums.Role;
import org.atlas.commons.util.UUIDGenerator;
import org.atlas.platform.auth.jwt.core.CustomClaims;
import org.atlas.platform.auth.jwt.core.JwtData;
import org.atlas.platform.auth.jwt.core.JwtService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class Auth0JwtService extends JwtService {

  public Auth0JwtService(RedisTemplate<String, Object> redisTemplate) {
    super(redisTemplate);
  }

  @Override
  public String issue(JwtData jwtData) {
    long now = System.currentTimeMillis();
    return JWT.create()
        .withJWTId(UUIDGenerator.generate())
        .withIssuer(jwtData.getIssuer())
        .withIssuedAt(new Date(now))
        .withSubject(jwtData.getSubject())
        .withAudience(jwtData.getAudience())
        .withExpiresAt(jwtData.getExpiredAt())
        .withClaim(CustomClaims.USER_ID, jwtData.getUserId())
        .withClaim(CustomClaims.USER_ROLE, jwtData.getUserRole().name())
        .sign(Algorithm.RSA256(publicKey, privateKey));
  }

  @Override
  public JwtData doParse(String jwt, String issuer) {
    DecodedJWT decodedJWT;
    try {
       decodedJWT = JWT.require(Algorithm.RSA256(publicKey))
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
        .userId(decodedJWT.getClaim(CustomClaims.USER_ID).asInt())
        .userRole(Role.valueOf(decodedJWT.getClaim(CustomClaims.USER_ROLE).asString()))
        .build();
  }
}
