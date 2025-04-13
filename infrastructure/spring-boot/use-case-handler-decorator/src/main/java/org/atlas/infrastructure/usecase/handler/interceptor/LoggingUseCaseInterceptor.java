package org.atlas.infrastructure.usecase.handler.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.context.UserContext;
import org.atlas.framework.context.UserInfo;
import org.atlas.framework.util.StopWatch;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
      log.debug("Anonymous user started handling use case {}: {}",
          useCaseClass.getSimpleName(), input);
    } else {
      log.debug("User {} started handling use case {}: {}",
          userInfo, useCaseClass.getSimpleName(), input);
    }
  }

  @Override
  public void postHandle(Class<?> useCaseClass, Object input) {
    UserInfo userInfo = UserContext.get();
    StopWatch stopWatch = STOP_WATCH_THREAD_LOCAL.get();

    // Stop the StopWatch
    stopWatch.stop();

    long elapsedTimeMillis = stopWatch.getElapsedTimeMillis();

    // Merge user info and performance check into one log statement
    String message;
    if (userInfo == null) {
      message = String.format("Anonymous user finished handling use case %s: %s in %d ms",
          useCaseClass.getSimpleName(), input, elapsedTimeMillis);
    } else {
      message = String.format("User %s finished handling use case %s: %s in %d ms",
          userInfo, useCaseClass.getSimpleName(), input, elapsedTimeMillis);
    }

    if (elapsedTimeMillis > ACCEPTED_MAX_EXECUTION_TIME_MS) {
      log.warn("{} ===> Exceeded max time, check performance!!!", message);
    } else {
      log.debug(message);
    }

    // Clean up: Reset the StopWatch and remove it from ThreadLocal
    STOP_WATCH_THREAD_LOCAL.remove();
  }
}
