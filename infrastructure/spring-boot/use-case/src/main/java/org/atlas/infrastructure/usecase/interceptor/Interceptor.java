package org.atlas.infrastructure.usecase.interceptor;

public interface Interceptor {

  void preHandle(Class<?> useCaseClass, Object input);

  void postHandle(Class<?> useCaseClass, Object input);
}
