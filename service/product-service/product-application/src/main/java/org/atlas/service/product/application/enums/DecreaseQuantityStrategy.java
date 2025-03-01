package org.atlas.service.product.application.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum DecreaseQuantityStrategy {

  CONSTRAINT("constraint"),
  OPTIMISTIC_LOCKING("optimistic_locking"),
  PESSIMISTIC_LOCKING("pessimistic_locking");

  private final String value;
}
