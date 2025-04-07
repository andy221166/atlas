package org.atlas.infrastructure.usecase.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.atlas.framework.transaction.TransactionPort;
import org.atlas.framework.usecase.input.InternalInput;
import org.atlas.infrastructure.usecase.handler.interceptor.UseCaseInterceptor;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class UseCaseHandlerAspect {

  private final List<UseCaseInterceptor> interceptors;
  private final TransactionPort transactionPort;

  @Around("execution(* org.atlas.framework.usecase.handler.UseCaseHandler.handle(..))")
  public Object aroundHandle(ProceedingJoinPoint joinPoint) {
    // Extract class and method details
    Class<?> useCaseHandlerClass = joinPoint.getTarget().getClass();

    // Retrieve input safely
    Object[] args = joinPoint.getArgs();
    InternalInput input =
        (args.length > 0 && args[0] instanceof InternalInput) ? (InternalInput) args[0] : null;

    // Execute pre-handle interceptors
    interceptors.forEach(interceptor -> interceptor.preHandle(useCaseHandlerClass, input));

    // Execute UseCaseHandler in a transaction manner
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
      interceptors.forEach(interceptor -> interceptor.postHandle(useCaseHandlerClass, input));
    }

    return result;
  }
}
