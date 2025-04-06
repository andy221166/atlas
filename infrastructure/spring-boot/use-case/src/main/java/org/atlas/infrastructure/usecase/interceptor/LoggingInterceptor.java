package org.atlas.infrastructure.usecase.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.context.UserContext;
import org.atlas.framework.context.UserInfo;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@Slf4j
public class LoggingInterceptor implements Interceptor {

  @Override
  public void preHandle(Class<?> useCaseClass, Object input) {
    UserInfo userInfo = UserContext.get();
    if (userInfo == null) {
      log.info("Anonymous user started executing use case {}: {}", useCaseClass, input);
    } else {
      log.info("User {} started executing use case {}: {}", userInfo, useCaseClass, input);
    }
  }

  @Override
  public void postHandle(Class<?> useCaseClass, Object input) {
    UserInfo userInfo = UserContext.get();
    if (userInfo == null) {
      log.info("Anonymous user finished executing use case {}: {}", useCaseClass, input);
    } else {
      log.info("User {} finished executing use case {}: {}", userInfo, useCaseClass, input);
    }
  }
}
