package org.atlas.commons.util.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Md5Util {

  public static String encode(String input) {
    return DigestUtils.md5Hex(input);
  }
}
