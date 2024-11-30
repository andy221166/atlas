package org.atlas.platform.cqrs.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.context.CurrentUser;
import org.atlas.commons.context.CurrentUserContext;
import org.atlas.platform.cqrs.model.Command;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommandLoggingInterceptor implements CommandInterceptor {

  @Override
  public <C extends Command<R>, R> void preHandle(C command) {
    CurrentUser currentUser = CurrentUserContext.getCurrentUser();
    if (currentUser == null) {
      log.info("Anonymous user executed command {}", command);
    } else {
      log.info("User {} executed command {}", currentUser, command);
    }
  }

  @Override
  public <C extends Command<R>, R> void postHandle(C command) {
    // Ignored
  }
}
