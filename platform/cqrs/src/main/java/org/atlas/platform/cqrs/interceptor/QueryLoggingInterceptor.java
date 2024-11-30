package org.atlas.platform.cqrs.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.context.CurrentUser;
import org.atlas.commons.context.CurrentUserContext;
import org.atlas.platform.cqrs.model.Query;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QueryLoggingInterceptor implements QueryInterceptor {

  @Override
  public <Q extends Query<R>, R> void preHandle(Q query) {
    CurrentUser currentUser = CurrentUserContext.getCurrentUser();
    if (currentUser == null) {
      log.info("Anonymous user executed query {}", query);
    } else {
      log.info("User {} executed query {}", currentUser, query);
    }
  }

  @Override
  public <Q extends Query<R>, R> void postHandle(Q query) {
    // Ignored
  }
}
