package org.atlas.infrastructure.lock.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LockAspect {

  private final LockService lockService;

  @Around("@annotation(lock)")
  public Object applyLock(ProceedingJoinPoint joinPoint, Lock lock)
      throws Throwable {
    boolean acquired = false;
    try {
      acquired = lockService.acquireLock(
          lock.key(), lock.timeout(), lock.timeUnit());
      if (acquired) {
        return joinPoint.proceed();
      } else {
        log.error("Failed to acquire lock {} for method {}",
            lock.key(), joinPoint.getSignature().toLongString());
        return null;
      }
    } finally {
      if (acquired) {
        lockService.releaseLock(lock.key());
      }
    }
  }
}
