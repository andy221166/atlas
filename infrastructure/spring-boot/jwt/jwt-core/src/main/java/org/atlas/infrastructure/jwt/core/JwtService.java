package org.atlas.infrastructure.jwt.core;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;

@RequiredArgsConstructor
@Slf4j
public abstract class JwtService implements InitializingBean {

  protected static final String JWT_PREFIX = "Bearer ";

  protected RSAPublicKey publicKey;
  protected RSAPrivateKey privateKey;

  @Override
  public void afterPropertiesSet() throws Exception {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    this.publicKey = loadPublicKey(keyFactory);
    this.privateKey = loadPrivateKey(keyFactory);
  }

  public abstract String generateJwt(JwtData jwtData);

  public abstract JwtData decodeJwt(String token, String issuer);

  private RSAPublicKey loadPublicKey(KeyFactory keyFactory)
      throws IOException, InvalidKeySpecException {
    try (InputStream inputStream = new ClassPathResource("jwt.pub").getInputStream()) {
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
    try (InputStream inputStream = new ClassPathResource("jwt.key").getInputStream()) {
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
}
