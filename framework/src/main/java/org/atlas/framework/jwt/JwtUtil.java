package org.atlas.framework.jwt;

import org.atlas.framework.jwt.auth0.Auth0JwtService;

/**
 * Implement Singleton pattern with Bill Pugh solution
 */
public class JwtUtil {

  private static class JwtServiceHolder {

    private static final JwtService INSTANCE = new Auth0JwtService();
  }

  public static JwtService getInstance() {
    return JwtServiceHolder.INSTANCE;
  }
}
