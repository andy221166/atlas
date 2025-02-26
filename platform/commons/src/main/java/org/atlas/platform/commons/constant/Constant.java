package org.atlas.platform.commons.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {

  // TODO: Consider to put it into config-server
  public static final String[] ALLOWED_HOSTS = {
      "http://localhost:9000" // Frontend
  };

  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static final String JWT_ISSUER = "atlas";
  public static final long JWT_EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 1 day in milliseconds
}
