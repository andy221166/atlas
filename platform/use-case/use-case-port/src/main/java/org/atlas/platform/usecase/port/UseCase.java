package org.atlas.platform.usecase.port;

public interface UseCase<I, O> {

  O handle(I input) throws Exception;
}
