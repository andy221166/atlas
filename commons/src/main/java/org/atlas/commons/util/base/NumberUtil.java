package org.atlas.commons.util.base;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberUtil {

  public static boolean isZero(Integer input) {
    if (input == null) {
      return false;
    }
    return input == 0;
  }

  public static boolean isZero(Long input) {
    if (input == null) {
      return false;
    }
    return input == 0L;
  }

  public static boolean isZero(BigDecimal input) {
    if (input == null) {
      return false;
    }
    return input.compareTo(BigDecimal.ZERO) == 0;
  }
}
