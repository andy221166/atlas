package org.atlas.framework.usecase.handler;

public interface UseCaseHandler<I, O> {

  O handle(I input) throws Exception;
}
