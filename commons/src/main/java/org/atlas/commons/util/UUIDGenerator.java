package org.atlas.commons.util;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UUIDGenerator {

  public static String generate() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
