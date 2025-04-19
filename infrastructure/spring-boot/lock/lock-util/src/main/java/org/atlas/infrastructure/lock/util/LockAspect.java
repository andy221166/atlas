package org.atlas.infrastructure.lock.util;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.atlas.framework.lock.LockPort;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LockAspect {

  private final LockPort lockPort;

  @Around("@annotation(lock)")
  public Object applyLock(ProceedingJoinPoint joinPoint, Lock lock)
      throws Throwable {
    boolean acquired = false;
    try {
      acquired = lockPort.acquireLock(lock.key(), Duration.of(lock.timeout(),
          lock.timeUnit().toChronoUnit()));
      if (acquired) {
        return joinPoint.proceed();
      } else {
        log.error("Failed to acquire lock {} for method {}",
            lock.key(), joinPoint.getSignature().toLongString());
        return null;
      }
    } finally {
      if (acquired) {
        lockPort.releaseLock(lock.key());
      }
    }
  }
}
