package org.atlas.platform.cqrs.exception;

public class QueryExecutorNotFoundException extends RuntimeException {

  public QueryExecutorNotFoundException(String message) {
    super(message);
  }
}
