package org.atlas.platform.lock.core;

import java.util.concurrent.TimeUnit;

public interface DistributedLockService {

    boolean acquireLock(String key, long timeout, TimeUnit timeUnit);
    void releaseLock(String key);
}
