package org.atlas.platform.commons.util;

import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UUIDGenerator {

  public static String generate() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
