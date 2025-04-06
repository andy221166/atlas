package org.atlas.infrastructure.json;

import org.atlas.infrastructure.json.jackson.JacksonAdapter;

/**
 * Implement Singleton pattern with Bill Pugh solution
 */
public class JsonUtil {

  private static class JsonHolder {

    private static final Json INSTANCE = new JacksonAdapter();
  }

  public static Json getInstance() {
    return JsonHolder.INSTANCE;
  }
}
