package org.atlas.platform.json;

import org.atlas.platform.json.jackson.JacksonAdapter;

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
