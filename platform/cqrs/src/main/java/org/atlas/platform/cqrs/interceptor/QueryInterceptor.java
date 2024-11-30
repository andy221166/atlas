package org.atlas.platform.cqrs.interceptor;

import org.atlas.platform.cqrs.model.Query;

public interface QueryInterceptor {

  <Q extends Query<R>, R> void preHandle(Q query);

  <Q extends Query<R>, R> void postHandle(Q query);
}
