package org.atlas.framework.usecase;

public interface UseCaseHandler<I, O> {

  O handle(I input) throws Exception;
}
