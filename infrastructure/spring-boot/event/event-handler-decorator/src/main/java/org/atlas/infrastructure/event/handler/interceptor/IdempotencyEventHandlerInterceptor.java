package org.atlas.infrastructure.event.handler.interceptor;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.event.DomainEvent;
import org.atlas.framework.lock.LockPort;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class IdempotencyEventHandlerInterceptor implements EventHandlerInterceptor {

  private final ApplicationConfigPort applicationConfigPort;
  private final LockPort lockPort;
  private final RedisTemplate<String, Object> redisTemplate;

  /**
   * Pre-handle logic to ensure the event is not processed more than once.
   * Steps:
   * 1. Check if the event has already been marked as processed using Redis.
   *    - If yes, throw an exception to prevent re-processing.
   * 2. Try to acquire a distributed lock (short TTL, e.g., 15 minutes).
   *    - This prevents multiple clients from processing the same event concurrently.
   *    - If the lock is not acquired, it means another client is currently processing it.
   */
  @Override
  public void preHandle(DomainEvent event) {
    String processedKey = redisKey(event);
    String lockKey = processedKey + ":lock";

    if (redisTemplate.hasKey(processedKey)) {
      throw new IllegalStateException(
          String.format("Event %s has already been processed", event.getEventId()));
    }

    if (!lockPort.acquireLock(lockKey, Duration.ofMinutes(15))) {
      throw new IllegalStateException(
          String.format("Event %s is already being processed", event.getEventId()));
    }
  }

  /**
   * Post-handle logic to update event state after processing.
   * Steps:
   * 1. If the event was successfully processed, store a marker key in Redis
   *    to prevent future re-processing (with a long TTL, e.g., 7 days).
   * 2. Release the previously acquired lock to allow cleanup.
   *    - Optional if the lock has a TTL, but still good for cleanup.
   */
  @Override
  public void postHandle(DomainEvent event) {
    String processedKey = redisKey(event);
    String lockKey = processedKey + ":lock";

    if (event.isProcessed()) {
      redisTemplate.opsForValue().set(processedKey, Duration.ofDays(7));
    }

    lockPort.releaseLock(lockKey);
  }

  private String redisKey(DomainEvent event) {
    return String.format("event:%s:%s",
        event.getEventId(), applicationConfigPort.getApplicationName());
  }
}
