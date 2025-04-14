package org.atlas.edge.auth.springsecurityjwt.service;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.edge.auth.springsecurityjwt.security.UserDetailsImpl;
import org.atlas.framework.constant.SecurityConstant;
import org.atlas.framework.jwt.DecodeJwtInput;
import org.atlas.framework.jwt.EncodeJwtInput;
import org.atlas.framework.jwt.Jwt;
import org.atlas.framework.jwt.JwtUtil;
import org.atlas.framework.security.cryptography.RsaKeyLoader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

  private final RedisTemplate<String, Object> redisTemplate;

  public String issueAccessToken(UserDetailsImpl userDetails)
      throws IOException, InvalidKeySpecException {
    Date issuedAt = new Date();
    Date expiresAt = new Date(
        issuedAt.getTime() + SecurityConstant.ACCESS_TOKEN_EXPIRATION_TIME * 1000);
    Jwt jwt = Jwt.builder()
        .issuer(SecurityConstant.TOKEN_ISSUER)
        .issuedAt(issuedAt)
        .subject(String.valueOf(userDetails.getUsername()))
        .audience(SecurityConstant.TOKEN_AUDIENCE)
        .expiredAt(expiresAt)
        .userId(userDetails.getUserId())
        .userRole(userDetails.getRole())
        .build();
    EncodeJwtInput input = EncodeJwtInput.builder()
        .jwt(jwt)
        .rsaPublicKey(RsaKeyLoader.loadPublicKey(SecurityConstant.RSA_PUBLIC_KEY_PATH))
        .rsaPrivateKey(RsaKeyLoader.loadPrivateKey(SecurityConstant.RSA_PRIVATE_KEY_PATH))
        .build();
    return JwtUtil.getInstance().encodeJwt(input);
  }

  public String issueRefreshToken(UserDetailsImpl userDetails)
      throws IOException, InvalidKeySpecException {
    Date issuedAt = new Date();
    Date expiresAt = new Date(
        issuedAt.getTime() + SecurityConstant.REFRESH_TOKEN_EXPIRATION_TIME * 1000);
    Jwt jwt = Jwt.builder()
        .issuer(SecurityConstant.TOKEN_ISSUER)
        .issuedAt(issuedAt)
        .subject(String.valueOf(userDetails.getUsername()))
        .audience(SecurityConstant.TOKEN_AUDIENCE)
        .expiredAt(expiresAt)
        .build();
    EncodeJwtInput input = EncodeJwtInput.builder()
        .jwt(jwt)
        .rsaPublicKey(RsaKeyLoader.loadPublicKey(SecurityConstant.RSA_PUBLIC_KEY_PATH))
        .rsaPrivateKey(RsaKeyLoader.loadPrivateKey(SecurityConstant.RSA_PRIVATE_KEY_PATH))
        .build();
    return JwtUtil.getInstance().encodeJwt(input);
  }

  public Jwt parseToken(String token) throws IOException, InvalidKeySpecException {
    DecodeJwtInput input = DecodeJwtInput.builder()
        .jwt(token)
        .issuer(SecurityConstant.TOKEN_ISSUER)
        .rsaPublicKey(RsaKeyLoader.loadPublicKey(SecurityConstant.RSA_PUBLIC_KEY_PATH))
        .build();
    return JwtUtil.getInstance().decodeJwt(input);
  }

  public void revokeToken(String token) throws IOException, InvalidKeySpecException {
    // Calculate TTL
    Jwt jwt = parseToken(token);
    long remainingTimeMillis = jwt.getExpiredAt().getTime() - System.currentTimeMillis();
    redisTemplate.opsForValue()
        .set(tokenBlacklistRedisKey(token), true, Duration.ofMillis(remainingTimeMillis));
    log.info("The token has been revoked: {}", token);
  }

  public boolean isBlacklist(String token) {
    return redisTemplate.hasKey(tokenBlacklistRedisKey(token));
  }

  private String tokenBlacklistRedisKey(String token) {
    return SecurityConstant.TOKEN_BLACKLIST_REDIS_KEY_PREFIX + token;
  }
}
