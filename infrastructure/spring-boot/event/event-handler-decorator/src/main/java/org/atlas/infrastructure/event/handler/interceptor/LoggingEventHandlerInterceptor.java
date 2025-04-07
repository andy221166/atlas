package org.atlas.infrastructure.event.handler.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.event.DomainEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Order(1)
@Slf4j
public class LoggingEventHandlerInterceptor implements EventHandlerInterceptor {

  // ThreadLocal to store StopWatch per thread
  private static final ThreadLocal<StopWatch> STOP_WATCH_THREAD_LOCAL =
      ThreadLocal.withInitial(StopWatch::new);

  @Override
  public void preHandle(DomainEvent event) {
    StopWatch stopWatch = STOP_WATCH_THREAD_LOCAL.get();
    stopWatch.start();
    log.debug("Started handling event {}", event);
  }

  @Override
  public void postHandle(DomainEvent event) {
    StopWatch stopWatch = STOP_WATCH_THREAD_LOCAL.get();
    stopWatch.stop();
    // Get the execution time in milliseconds
    long timeElapsedMs = stopWatch.getTotalTimeMillis();
    log.debug("Finished handling event {}. Elapsed time: {} ms", event.getEventId(), timeElapsedMs);
    // Clean up: Reset the StopWatch and remove it from ThreadLocal
    STOP_WATCH_THREAD_LOCAL.remove();
  }
}
