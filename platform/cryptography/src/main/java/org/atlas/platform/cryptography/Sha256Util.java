package org.atlas.platform.cryptography;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Sha256Util {

  public static String encode(String input) {
    return DigestUtils.sha256Hex(input);
  }
}
