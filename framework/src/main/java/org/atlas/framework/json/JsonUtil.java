package org.atlas.framework.json;

import org.atlas.framework.json.jackson.JacksonService;

/**
 * Implement Singleton pattern with Bill Pugh solution
 */
public class JsonUtil {

  private static class ServiceHolder {

    private static final JsonService INSTANCE = new JacksonService();
  }

  public static JsonService getInstance() {
    return ServiceHolder.INSTANCE;
  }
}
