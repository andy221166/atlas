package org.atlas.edge.gateway.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TokenService {

  private static final String TOKEN_PREFIX = "Bearer";
  private static final String ISSUER = "my-app";

  private RSAPublicKey publicKey;

  @PostConstruct
  public void init() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    this.publicKey = loadPublicKey(keyFactory);
  }

  public TokenInfo verifyToken(String authorizationHeader) {
    String token = extractTokenFromHeader(authorizationHeader);

    DecodedJWT decodedJWT = JWT.require(Algorithm.RSA256(publicKey))
        .withIssuer(ISSUER)
        .build()
        .verify(token);

    return new TokenInfo(decodedJWT);
  }

  private RSAPublicKey loadPublicKey(KeyFactory keyFactory)
      throws IOException, InvalidKeySpecException {
    try (InputStream inputStream = new ClassPathResource("secret/token.pub").getInputStream()) {
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

  private String extractTokenFromHeader(String authorizationHeader) {
    if (!authorizationHeader.startsWith(TOKEN_PREFIX)) {
      throw new RuntimeException("Invalid token prefix");
    }
    final String accessToken = authorizationHeader.substring(TOKEN_PREFIX.length()).trim();
    if (!StringUtils.hasText(accessToken)) {
      throw new RuntimeException("Empty token after truncating token prefix");
    }
    return accessToken;
  }
}
