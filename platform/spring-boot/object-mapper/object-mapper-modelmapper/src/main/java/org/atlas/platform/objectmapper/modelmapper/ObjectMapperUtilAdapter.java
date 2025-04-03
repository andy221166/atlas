package org.atlas.platform.objectmapper.modelmapper;

import org.atlas.platform.objectmapper.ObjectMapper;
import org.atlas.platform.objectmapper.ObjectMapperUtil;

/**
 * Implement Singleton pattern with Bill Pugh solution
 */
public class ObjectMapperUtilAdapter implements ObjectMapperUtil {

  @Override
  public ObjectMapper getInstance() {
    return ObjectMapperHolder.INSTANCE;
  }

  private static class ObjectMapperHolder {

    private static final ObjectMapper INSTANCE = new ObjectMapperAdapter();
  }
}
