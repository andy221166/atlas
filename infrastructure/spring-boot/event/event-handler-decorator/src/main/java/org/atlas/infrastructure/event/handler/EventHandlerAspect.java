package org.atlas.infrastructure.event.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.atlas.framework.event.DomainEvent;
import org.atlas.framework.transaction.TransactionPort;
import org.atlas.infrastructure.event.handler.interceptor.EventHandlerInterceptor;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class EventHandlerAspect {

  private final List<EventHandlerInterceptor> interceptors;
  private final TransactionPort transactionPort;

  @Around("execution(* org.atlas.framework.event.handler.EventHandler.handle(..))")
  public Object aroundHandle(ProceedingJoinPoint joinPoint) {
    // Retrieve input safely
    Object[] args = joinPoint.getArgs();
    DomainEvent event = (DomainEvent) args[0];

    // Execute pre-handle interceptors
    interceptors.forEach(interceptor -> interceptor.preHandle(event));

    // Execute EventHandler in a transaction manner
    Object result;
    try {
      result = transactionPort.execute(() -> {
        try {
          return joinPoint.proceed();
        } catch (Throwable e) {
          throw new RuntimeException(e);
        }
      });
    } finally {
      // Execute post-handle interceptors
      interceptors.forEach(interceptor -> interceptor.postHandle(event));
    }

    return result;
  }
}
