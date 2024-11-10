package org.atlas.platform.lock.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.lock.core.DistributedLockService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLockService implements DistributedLockService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean acquireLock(String key, long timeout, TimeUnit timeUnit) {
        boolean acquired = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, "locked", timeout, timeUnit));
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
