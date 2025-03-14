package org.atlas.platform.sequencegenerator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SequenceType {

  PRODUCT("product", "PRD", 4),
  ORDER("order", "ORD", 4);

  private final String name;
  private final String prefix;
  private final int padding;
}
