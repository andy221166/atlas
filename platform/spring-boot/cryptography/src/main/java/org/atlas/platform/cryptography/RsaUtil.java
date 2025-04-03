package org.atlas.platform.cryptography;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RsaUtil {

  public static void generate(String publicKeyPath, String privateKeyPath)
      throws IOException, NoSuchAlgorithmException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();

    // Public key
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    Writer out = new FileWriter(publicKeyPath);
    out.write("-----BEGIN PUBLIC KEY-----\n");
    Base64.Encoder encoder = Base64.getEncoder();
    out.write(encoder.encodeToString(publicKey.getEncoded()));
    out.write("\n-----END PUBLIC KEY-----\n");
    out.close();

    // Private key
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    out = new FileWriter(privateKeyPath);
    out.write("-----BEGIN PRIVATE KEY-----\n");
    out.write(encoder.encodeToString(privateKey.getEncoded()));
    out.write("\n-----END PRIVATE KEY-----\n");
    out.close();
  }
}
