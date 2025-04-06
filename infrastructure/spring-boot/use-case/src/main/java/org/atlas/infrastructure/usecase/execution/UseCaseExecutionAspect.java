package org.atlas.infrastructure.usecase.execution;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.atlas.framework.usecase.input.InternalInput;
import org.atlas.infrastructure.usecase.interceptor.Interceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class UseCaseExecutionAspect {

  private final List<Interceptor> interceptors;

  @Value("${app.use_case.max_execution_time:3000}") // Changed to milliseconds for clarity
  private long acceptedMaxExecutionTime;

  @Around("execution(* org.atlas.framework.usecase.UseCaseHandler.handle(..))")
  public Object aroundHandle(ProceedingJoinPoint joinPoint) throws Throwable {
    // Extract class and method details
    Class<?> useCaseClass = joinPoint.getTarget().getClass();

    // Retrieve input safely
    Object[] args = joinPoint.getArgs();
    InternalInput input =
        (args.length > 0 && args[0] instanceof InternalInput) ? (InternalInput) args[0] : null;

    // Execute pre-handle interceptors
    interceptors.forEach(interceptor -> interceptor.preHandle(useCaseClass, input));

    // Execute use case and measure execution time
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    Object result;
    try {
      result = joinPoint.proceed();
    } finally {
      stopWatch.stop();
      long timeElapsed = stopWatch.getTotalTimeMillis();
      if (timeElapsed > acceptedMaxExecutionTime) {
        log.warn(
            "Finished executing use case {}. Input: {}. Elapsed time: {} ms ===> Exceeded max time, check performance!!!",
            useCaseClass, input, timeElapsed);
      }
    }

    // Execute post-handle interceptors
    interceptors.forEach(interceptor -> interceptor.postHandle(useCaseClass, input));

    return result;
  }
}
