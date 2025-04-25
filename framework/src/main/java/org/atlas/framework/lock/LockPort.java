package org.atlas.framework.lock;

import java.time.Duration;

public interface LockPort {

  void acquireLock(String key, Duration timeout) throws LockAcquisitionException;

  void releaseLock(String key);
}
