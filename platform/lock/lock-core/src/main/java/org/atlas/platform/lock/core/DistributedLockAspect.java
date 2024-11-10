package org.atlas.platform.lock.core;

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
public class DistributedLockAspect {

    private final DistributedLockService distributedLockService;

    @Around("@annotation(distributedLock)")
    public Object applyLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        boolean acquired = false;
        try {
            acquired = distributedLockService.acquireLock(
                    distributedLock.key(), distributedLock.timeout(), distributedLock.timeUnit());
            if (acquired) {
                return joinPoint.proceed();
            } else {
                log.error("Failed to acquire lock {} for method {}",
                        distributedLock.key(), joinPoint.getSignature().toLongString());
                return null;
            }
        } finally {
            if (acquired) {
                distributedLockService.releaseLock(distributedLock.key());
            }
        }
    }
}
