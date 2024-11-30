package org.atlas.platform.cqrs.exception;

public class CommandExecutorNotFoundException extends RuntimeException {

  public CommandExecutorNotFoundException(String message) {
    super(message);
  }
}
