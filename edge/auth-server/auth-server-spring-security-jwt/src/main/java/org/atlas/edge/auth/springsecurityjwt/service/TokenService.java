package org.atlas.edge.auth.springsecurityjwt.service;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.edge.auth.springsecurityjwt.security.UserDetailsImpl;
import org.atlas.framework.constant.SecurityConstant;
import org.atlas.framework.security.cryptography.RsaKeyLoader;
import org.atlas.framework.jwt.DecodeJwtInput;
import org.atlas.framework.jwt.DecodeJwtOutput;
import org.atlas.framework.jwt.EncodeJwtInput;
import org.atlas.framework.jwt.JwtUtil;
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
    Date expiresAt = new Date(issuedAt.getTime() + SecurityConstant.ACCESS_TOKEN_EXPIRATION_TIME);
    EncodeJwtInput encodeJwtInput = EncodeJwtInput.builder()
        .issuer(SecurityConstant.TOKEN_ISSUER)
        .issuedAt(issuedAt)
        .subject(String.valueOf(userDetails.getUserId()))
        .audience(SecurityConstant.TOKEN_AUDIENCE)
        .expiredAt(expiresAt)
        .rsaPublicKey(RsaKeyLoader.loadPublicKey(SecurityConstant.RSA_PUBLIC_KEY_PATH))
        .rsaPrivateKey(RsaKeyLoader.loadPrivateKey(SecurityConstant.RSA_PRIVATE_KEY_PATH))
        .userId(userDetails.getUserId())
        .userRole(userDetails.getRole())
        .build();
    return JwtUtil.getInstance().encodeJwt(encodeJwtInput);
  }

  public String issueRefreshToken(UserDetailsImpl userDetails) {
    return null;
  }

  public void revokeAccessToken(String accessToken) throws IOException, InvalidKeySpecException {
    // Calculate TTL
    DecodeJwtInput decodeJwtInput = DecodeJwtInput.builder()
        .jwt(accessToken)
        .issuer(SecurityConstant.TOKEN_ISSUER)
        .rsaPublicKey(RsaKeyLoader.loadPublicKey(SecurityConstant.RSA_PUBLIC_KEY_PATH))
        .build();
    DecodeJwtOutput decodeJwtOutput = JwtUtil.getInstance().decodeJwt(decodeJwtInput);
    long remainingTimeMillis =
        decodeJwtOutput.getExpiredAt().getTime() - System.currentTimeMillis();
    String blacklistRedisKey = getBlacklistRedisKey(accessToken);
    redisTemplate.opsForValue()
        .set(blacklistRedisKey, true, remainingTimeMillis, TimeUnit.MILLISECONDS);
    log.info("The access token has been revoked: {}", accessToken);
  }

  private String getBlacklistRedisKey(String jwt) {
    return "jwt_blacklist:" + jwt;
  }
}
