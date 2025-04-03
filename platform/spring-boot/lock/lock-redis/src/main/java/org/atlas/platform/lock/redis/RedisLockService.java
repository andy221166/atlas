package org.atlas.platform.lock.redis;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.lock.core.LockService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLockService implements LockService {

  private static final String LOCK_VALUE = "locked";

  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public boolean acquireLock(String key) {
    return this.acquireLock(key, 0L, null);
  }

  @Override
  public boolean acquireLock(String key, long timeout, TimeUnit timeUnit) {
    boolean acquired;
    if (timeout > 0) {
      acquired = Boolean.TRUE.equals(
          redisTemplate.opsForValue().setIfAbsent(key, LOCK_VALUE, timeout, timeUnit));
    } else {
      acquired = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, LOCK_VALUE));
    }
    if (acquired) {
      log.info("Acquired lock {} in {} {}", key, timeout, timeUnit);
    }
    return acquired;
  }

  @Override
  public void releaseLock(String key) {
    redisTemplate.delete(key);
    log.info("Released lock {}", key);
  }
}
