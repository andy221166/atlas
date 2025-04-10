package org.atlas.framework.jwt;

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
import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.util.FileUtil;

@Slf4j
public abstract class JwtService {

  protected static final String JWT_PREFIX = "Bearer ";

  protected static final RSAPublicKey RSA_PUBLIC_KEY;
  protected static final RSAPrivateKey RSA_PRIVATE_KEY;

  static {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      RSA_PUBLIC_KEY = loadPublicKey(keyFactory);
      RSA_PRIVATE_KEY = loadPrivateKey(keyFactory);
    } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }
  }

  public abstract String generateJwt(JwtData jwtData);

  public abstract JwtData decodeJwt(String token, String issuer);

  private static RSAPublicKey loadPublicKey(KeyFactory keyFactory)
      throws IOException, InvalidKeySpecException {
    try (InputStream inputStream = FileUtil.readResourceFileAsStream("jwt.pub")) {
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

  private static RSAPrivateKey loadPrivateKey(KeyFactory keyFactory)
      throws IOException, InvalidKeySpecException {
    try (InputStream inputStream = FileUtil.readResourceFileAsStream("jwt.key")) {
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
