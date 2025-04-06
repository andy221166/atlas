package org.atlas.infrastructure.lock.core;

import java.util.concurrent.TimeUnit;

public interface LockService {

  boolean acquireLock(String key);

  boolean acquireLock(String key, long timeout, TimeUnit timeUnit);

  void releaseLock(String key);
}
