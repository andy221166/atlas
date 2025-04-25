package org.atlas.infrastructure.lock.redis;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.lock.LockAcquisitionException;
import org.atlas.framework.lock.LockPort;
import org.atlas.framework.resilience.RetryUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLockService implements LockPort {

  private static final String LOCK_VALUE = "locked";

  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public void acquireLock(String key, Duration timeout) throws LockAcquisitionException {
    RetryUtil.retryOn(() -> {
      boolean acquired = Boolean.TRUE.equals(
          redisTemplate.opsForValue().setIfAbsent(key, LOCK_VALUE, timeout));
      if (acquired) {
        log.info("Acquired lock {} in {}", key, timeout);
      } else {
        throw new LockAcquisitionException();
      }
    }, LockAcquisitionException.class);
  }

  @Override
  public void releaseLock(String key) {
    redisTemplate.delete(key);
    log.info("Released lock {}", key);
  }
}
