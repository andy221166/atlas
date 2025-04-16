package org.atlas.domain.product.shared.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum DecreaseQuantityStrategy {

  CONSTRAINT("constraint"),
  OPTIMISTIC_LOCKING("optimistic_locking"),
  PESSIMISTIC_LOCKING("pessimistic_locking"),
  REDIS_LOCK("redis_lock");

  private final String value;

  public static DecreaseQuantityStrategy fromValue(String value) {
    for (DecreaseQuantityStrategy strategy : values()) {
      if (strategy.value.equals(value)) {
        return strategy;
      }
    }
    throw new IllegalArgumentException(value);
  }
}
