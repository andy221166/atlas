package org.atlas.framework.lock;

import java.time.Duration;

public interface LockPort {

  default boolean acquireLock(String key) {
    return acquireLock(key, Duration.ZERO);
  }

  boolean acquireLock(String key, Duration timeout);

  void releaseLock(String key);
}
