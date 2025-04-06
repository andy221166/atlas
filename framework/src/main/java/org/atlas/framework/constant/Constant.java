package org.atlas.framework.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {

  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static final String JWT_ISSUER = "atlas.auth";
  public static final String JWT_AUDIENCE = "atlas";
  public static final long JWT_EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 1 day in milliseconds

  public static final int DEFAULT_PAGE_SIZE = 20;
  public static final String DEFAULT_PAGE_SIZE_STR = "20";
}
