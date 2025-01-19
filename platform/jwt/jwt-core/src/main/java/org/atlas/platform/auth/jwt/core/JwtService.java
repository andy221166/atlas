package org.atlas.platform.auth.jwt.core;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
@Slf4j
public abstract class JwtService {

  protected RSAPublicKey publicKey;
  protected RSAPrivateKey privateKey;

  protected final RedisTemplate<String, Object> redisTemplate;

  @PostConstruct
  public void init() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    this.publicKey = loadPublicKey(keyFactory);
    this.privateKey = loadPrivateKey(keyFactory);
  }

  public abstract String issue(JwtData jwtData);
  protected abstract JwtData doParse(String jwt, String issuer);

  public JwtData verify(String jwt, String issuer) {
    JwtData jwtData = parse(jwt, issuer);

    // Check blacklist
    String blacklistRedisKey = getBlacklistRedisKey(jwt);
    if (redisTemplate.opsForValue().get(blacklistRedisKey) != null) {
      throw new InvalidTokenException("The JWT token was inactivated");
    }

    return jwtData;
  }

  public void revoke(String jwt, String issuer) {
    JwtData jwtData = parse(jwt, issuer);

    // Calculate TTL
    long remainingTimeMillis = jwtData.getExpiredAt().getTime() - System.currentTimeMillis();
    String blacklistRedisKey = getBlacklistRedisKey(jwt);
    redisTemplate.opsForValue()
        .set(blacklistRedisKey, true, remainingTimeMillis, TimeUnit.MILLISECONDS);
    log.info("The JWT token has been revoked: {}", jwt);
  }

  private JwtData parse(String jwt, String issuer) {
    final String prefix = "Bearer ";
    if (jwt.startsWith(prefix)) {
      jwt = jwt.substring(prefix.length());
    }
    return doParse(jwt, issuer);
  }

  private RSAPublicKey loadPublicKey(KeyFactory keyFactory)
      throws IOException, InvalidKeySpecException {
    try (InputStream inputStream = new ClassPathResource("secret/jwt.pub").getInputStream()) {
      byte[] publicKeyBytes = inputStream.readAllBytes();
      String publicKeyContent = new String(publicKeyBytes)
          .replace("-----BEGIN PUBLIC KEY-----", "")
          .replace("-----END PUBLIC KEY-----", "")
          .replaceAll("\\r", "")
          .replaceAll("\\n", "");
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
          Base64.getDecoder().decode(publicKeyContent));
      return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }
  }

  private RSAPrivateKey loadPrivateKey(KeyFactory keyFactory)
      throws IOException, InvalidKeySpecException {
    try (InputStream inputStream = new ClassPathResource("secret/jwt.key").getInputStream()) {
      byte[] privateKeyBytes = inputStream.readAllBytes();
      String privateKeyContent = new String(privateKeyBytes)
          .replace("-----BEGIN PRIVATE KEY-----", "")
          .replace("-----END PRIVATE KEY-----", "")
          .replaceAll("\\r", "")
          .replaceAll("\\n", "");
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
          Base64.getDecoder().decode(privateKeyContent));
      return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }
  }

  private String getBlacklistRedisKey(String jwt) {
    return "jwt_blacklist:" + jwt;
  }
}
