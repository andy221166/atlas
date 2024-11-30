package org.atlas.platform.cqrs.monitoring;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Slf4j
public class QueryExecutionPerformanceAspect {

  @Value("${app.cqrs.query.max_execution_time:3}")
  private long acceptedMaxExecutionTime;

  @Around("execution(* org.atlas.platform.cqrs.handler.QueryHandler.handle(..))")
  public Object aroundHandle(ProceedingJoinPoint joinPoint) throws Throwable {
    // Get class name
    Class<?> clazz = joinPoint.getTarget().getClass();
    String className = clazz.getName();

    // Get method name
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    String methodName = method.getName();

    // Log method start
    log.info("Started executing query {}#{}", className, methodName);

    // Execute method and measure elapsed time
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    try {
      return joinPoint.proceed();
    } finally {
      stopWatch.stop();
      long timeElapsed = stopWatch.getTotalTimeMillis();
      if (timeElapsed <= acceptedMaxExecutionTime) {
        log.info("Finished executing query {}#{}. Elapsed time: {} ms.", className, methodName,
            timeElapsed);
      } else {
        log.warn(
            "Finished executing query {}#{}. Elapsed time: {} ms ===> Exceeded the maximum time, please check its performance!!!",
            className, methodName, timeElapsed);
      }
    }
  }
}
