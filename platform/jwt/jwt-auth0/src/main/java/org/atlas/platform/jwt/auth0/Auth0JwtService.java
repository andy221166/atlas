package org.atlas.platform.jwt.auth0;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.atlas.platform.jwt.core.JwtData;
import org.atlas.platform.jwt.core.JwtService;
import org.atlas.platform.commons.enums.CustomClaim;
import org.atlas.platform.commons.enums.Role;
import org.atlas.platform.commons.util.UUIDGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class Auth0JwtService extends JwtService {

  public Auth0JwtService(RedisTemplate<String, Object> redisTemplate) {
    super(redisTemplate);
  }

  @Override
  public String issueToken(JwtData jwtData) {
    return JWT.create()
        .withJWTId(UUIDGenerator.generate())
        .withIssuer(jwtData.getIssuer())
        .withIssuedAt(jwtData.getIssuedAt())
        .withSubject(jwtData.getSubject())
        .withAudience(jwtData.getAudience())
        .withExpiresAt(jwtData.getExpiredAt())
        .withClaim(CustomClaim.USER_ID.value(), jwtData.getUserId())
        .withClaim(CustomClaim.USER_ROLE.value(), jwtData.getUserRole().name())
        .sign(Algorithm.RSA256(publicKey, privateKey));
  }

  @Override
  public JwtData doParseToken(String jwt, String issuer) {
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
        .userId(decodedJWT.getClaim(CustomClaim.USER_ID.value()).asInt())
        .userRole(Role.valueOf(decodedJWT.getClaim(CustomClaim.USER_ROLE.value()).asString()))
        .build();
  }
}
