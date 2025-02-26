package org.atlas.platform.objectmapper;

import org.atlas.platform.objectmapper.modelmapper.ModelMapperAdapter;

/**
 * Implement Singleton pattern with Bill Pugh solution
 */
public class ObjectMapperUtil {

  private static class ObjectMapperHolder {

    private static final ObjectMapper INSTANCE = new ModelMapperAdapter();
  }

  public static ObjectMapper getInstance() {
    return ObjectMapperHolder.INSTANCE;
  }
}
