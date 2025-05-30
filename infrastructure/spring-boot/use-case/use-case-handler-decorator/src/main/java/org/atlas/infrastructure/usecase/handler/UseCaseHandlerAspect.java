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
  public Object aroundHandle(ProceedingJoinPoint joinPoint) throws Throwable {
    // Extract class and method details
    Class<?> useCaseHandlerClass = joinPoint.getTarget().getClass();

    // Retrieve input
    Object[] args = joinPoint.getArgs();

    // Execute pre-handle interceptors
    interceptors.forEach(interceptor ->
        interceptor.preHandle(useCaseHandlerClass, args[0]));

    // Execute UseCaseHandler in a transaction manner
    transactionPort.begin();
    try {
      Object result = joinPoint.proceed();
      transactionPort.commit();
      return result;
    } catch (Throwable e) {
      transactionPort.rollback();
      throw e;
    } finally {
      // Execute post-handle interceptors
      interceptors.forEach(interceptor ->
          interceptor.postHandle(useCaseHandlerClass, args[0]));
    }
  }
}
