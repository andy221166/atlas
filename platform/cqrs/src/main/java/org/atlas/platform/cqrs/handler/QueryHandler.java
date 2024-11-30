package org.atlas.platform.cqrs.handler;

import org.atlas.platform.cqrs.model.Query;

public interface QueryHandler<Q extends Query<R>, R> {

  R handle(Q query) throws Exception;
}
