package org.atlas.infrastructure.lock.redis;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.lock.LockPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLockService implements LockPort {

  private static final String LOCK_VALUE = "locked";

  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public boolean acquireLock(String key, long timeout, TimeUnit timeUnit) {
    boolean acquired = Boolean.TRUE.equals(
        redisTemplate.opsForValue().setIfAbsent(key, LOCK_VALUE, timeout, timeUnit));
    if (acquired) {
      log.info("Acquired lock {} in {}", key, timeout);
    }
    return acquired;
  }

  @Override
  public void releaseLock(String key) {
    redisTemplate.delete(key);
    log.info("Released lock {}", key);
  }
}
