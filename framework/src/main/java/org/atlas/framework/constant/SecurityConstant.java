package org.atlas.framework.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityConstant {

  // Relative path to resources folder
  public static final String RSA_PUBLIC_KEY_PATH = "secret/default.pub";
  public static final String RSA_PRIVATE_KEY_PATH = "secret/default.key";

  // Token
  public static final String TOKEN_ISSUER = "atlas";
  public static final String TOKEN_AUDIENCE = "atlas";
  public static final long ACCESS_TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 1 day in milliseconds
  public static final long REFRESH_TOKEN_EXPIRATION_TIME = 30L * 24 * 60 * 60 * 1000; // 30 days in milliseconds
  public static final String TOKEN_BLACKLIST_REDIS_KEY_PREFIX = "token:blacklist:";
  public static final String JWKS_KEY_ID = "atlas";
}
