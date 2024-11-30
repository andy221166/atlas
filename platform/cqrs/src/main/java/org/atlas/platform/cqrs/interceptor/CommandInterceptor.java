package org.atlas.platform.cqrs.interceptor;

import org.atlas.platform.cqrs.model.Command;

public interface CommandInterceptor {

  <C extends Command<R>, R> void preHandle(C command);

  <C extends Command<R>, R> void postHandle(C command);
}
