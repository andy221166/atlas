package org.atlas.commons.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {

  public static final String[] ALLOWED_HOSTS = {
      "http://localhost:9000" // Frontend
  };
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
