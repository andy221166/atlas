package org.atlas.infrastructure.usecase.handler.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.security.UserContext;
import org.atlas.framework.security.model.UserInfo;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Order(1)
@Slf4j
public class LoggingUseCaseInterceptor implements UseCaseInterceptor {

  private static final long ACCEPTED_MAX_EXECUTION_TIME_MS = 3000; // 3 seconds

  // ThreadLocal to store StopWatch per thread
  private static final ThreadLocal<StopWatch> STOP_WATCH_THREAD_LOCAL =
      ThreadLocal.withInitial(StopWatch::new);

  @Override
  public void preHandle(Class<?> useCaseClass, Object input) {
    UserInfo userInfo = UserContext.get();
    StopWatch stopWatch = STOP_WATCH_THREAD_LOCAL.get();

    // Start the StopWatch
    stopWatch.start();

    if (userInfo == null) {
      log.debug("Anonymous user started handling use case {}: {}", useCaseClass, input);
    } else {
      log.debug("User {} started handling use case {}: {}", userInfo, useCaseClass, input);
    }
  }

  @Override
  public void postHandle(Class<?> useCaseClass, Object input) {
    UserInfo userInfo = UserContext.get();
    StopWatch stopWatch = STOP_WATCH_THREAD_LOCAL.get();

    // Stop the StopWatch
    stopWatch.stop();

    // Get the execution time in milliseconds
    long timeElapsedMs = stopWatch.getTotalTimeMillis();

    // Merge user info and performance check into one log statement
    String message;
    if (userInfo == null) {
      message = "Anonymous user finished handling use case " + useCaseClass.getSimpleName() + ": " + input + " in " + timeElapsedMs + " ms";
    } else {
      message = "User " + userInfo + " finished handling use case " + useCaseClass.getSimpleName() + ": " + input + " in " + timeElapsedMs + " ms";
    }

    if (timeElapsedMs > ACCEPTED_MAX_EXECUTION_TIME_MS) {
      log.warn("{} ===> Exceeded max time, check performance!!!", message);
    } else {
      log.debug(message);
    }

    // Clean up: Reset the StopWatch and remove it from ThreadLocal
    STOP_WATCH_THREAD_LOCAL.remove();
  }
}
