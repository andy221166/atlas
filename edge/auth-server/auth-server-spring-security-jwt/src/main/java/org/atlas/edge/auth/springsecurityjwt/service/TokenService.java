package org.atlas.edge.auth.springsecurityjwt.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.edge.auth.springsecurityjwt.security.UserDetailsImpl;
import org.atlas.edge.auth.springsecurityjwt.exception.InvalidTokenException;
import org.atlas.framework.jwt.JwtData;
import org.atlas.framework.jwt.JwtService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

  public static final String TOKEN_ISSUER = "atlas.auth";
  public static final String TOKEN_AUDIENCE = "atlas";
  public static final long ACCESS_TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 1 day in milliseconds

  private final JwtService jwtService;
  private final RedisTemplate<String, Object> redisTemplate;

  public String issueAccessToken(UserDetailsImpl userDetails) {
    Date issuedAt = new Date();
    Date expiresAt = new Date(issuedAt.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);
    JwtData jwtData = JwtData.builder()
        .issuer(TOKEN_ISSUER)
        .issuedAt(issuedAt)
        .subject(String.valueOf(userDetails.getUserId()))
        .audience(TOKEN_AUDIENCE)
        .expiredAt(expiresAt)
        .userId(userDetails.getUserId())
        .userRole(userDetails.getRole())
        .build();
    return jwtService.generateJwt(jwtData);
  }

  public String issueRefreshToken(UserDetailsImpl userDetails) {
    return null;
  }

  public JwtData verifyAccessToken(String accessToken) {
    // Try decoding access token to JwtData
    JwtData jwtData = jwtService.decodeJwt(accessToken, TOKEN_ISSUER);

    // Check blacklist
    String blacklistRedisKey = getBlacklistRedisKey(accessToken);
    if (redisTemplate.opsForValue().get(blacklistRedisKey) != null) {
      throw new InvalidTokenException("The access token was inactivated");
    }

    return jwtData;
  }

  public void revokeAccessToken(String accessToken) {
    // Calculate TTL
    JwtData jwtData = jwtService.decodeJwt(accessToken, TOKEN_ISSUER);
    long remainingTimeMillis = jwtData.getExpiredAt().getTime() - System.currentTimeMillis();
    String blacklistRedisKey = getBlacklistRedisKey(accessToken);
    redisTemplate.opsForValue()
        .set(blacklistRedisKey, true, remainingTimeMillis, TimeUnit.MILLISECONDS);
    log.info("The access token has been revoked: {}", accessToken);
  }

  private String getBlacklistRedisKey(String jwt) {
    return "jwt_blacklist:" + jwt;
  }
}
