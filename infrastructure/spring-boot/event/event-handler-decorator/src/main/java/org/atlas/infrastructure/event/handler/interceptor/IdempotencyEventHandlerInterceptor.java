package org.atlas.infrastructure.event.handler.interceptor;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.event.DomainEvent;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class IdempotencyEventHandlerInterceptor implements EventHandlerInterceptor {

  private final ApplicationConfigPort applicationConfigPort;
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public void preHandle(DomainEvent event) {
    if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey(event)))) {
      throw new IllegalStateException(
          String.format("Event %s has already been processed", event.getEventId()));
    }
  }

  @Override
  public void postHandle(DomainEvent event) {
    if (event.isProcessed()) {
      redisTemplate.opsForValue()
          .set(redisKey(event), "processed", Duration.ofDays(7));
    }
  }

  private String redisKey(DomainEvent event) {
    return String.format("event:processed:%s:%s",
        event.getEventId(), applicationConfigPort.getApplicationName());
  }
}
