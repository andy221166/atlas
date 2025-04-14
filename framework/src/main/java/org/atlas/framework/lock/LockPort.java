package org.atlas.framework.lock;

import java.util.concurrent.TimeUnit;

public interface LockPort {

  boolean acquireLock(String key, long timeout, TimeUnit timeUnit);

  void releaseLock(String key);
}
