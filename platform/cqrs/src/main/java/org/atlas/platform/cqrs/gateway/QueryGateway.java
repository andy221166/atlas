package org.atlas.platform.cqrs.gateway;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.atlas.platform.cqrs.exception.QueryExecutorNotFoundException;
import org.atlas.platform.cqrs.handler.QueryHandler;
import org.atlas.platform.cqrs.interceptor.QueryInterceptor;
import org.atlas.platform.cqrs.model.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueryGateway {

  private final Map<String, QueryHandler<?, ?>> executorMap;
  private final List<QueryInterceptor> interceptors;

  public <Q extends Query<R>, R> R send(Q query) throws Exception {
    QueryHandler<Q, R> executor = obtainExecutor(query);
    interceptors.forEach(interceptor -> interceptor.preHandle(query));
    R result = executor.handle(query);
    interceptors.forEach(interceptor -> interceptor.postHandle(query));
    return result;
  }

  @SuppressWarnings("unchecked")
  private <C extends Query<R>, R> QueryHandler<C, R> obtainExecutor(Query<R> query) {
    if (CollectionUtils.isEmpty(executorMap)) {
      throw new QueryExecutorNotFoundException("Not found executor for query " + query.getClass());
    }
    String executorBeanName =
        StringUtils.uncapitalize(query.getClass().getSimpleName()) + "Handler";
    QueryHandler<?, ?> executor = executorMap.get(executorBeanName);
    if (executor == null) {
      throw new QueryExecutorNotFoundException("Not found executor for query " + query.getClass());
    }
    return (QueryHandler<C, R>) executor;
  }
}
