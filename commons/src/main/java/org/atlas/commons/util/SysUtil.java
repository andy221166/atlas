package org.atlas.commons.util;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SysUtil {

  /**
   * @return environment variable value
   */
  public static Optional<String> getEnv(String name) {
    return Optional.ofNullable(System.getenv(name));
  }
}
