package org.atlas.commons.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomHeaders {

  public static final String USER_ID = "X-User-Id";
  public static final String USER_ROLE = "X-User-Role";
}
