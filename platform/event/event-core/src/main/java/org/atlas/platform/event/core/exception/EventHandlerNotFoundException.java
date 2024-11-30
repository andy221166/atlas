package org.atlas.platform.event.core.exception;

public class EventHandlerNotFoundException extends RuntimeException {

  public EventHandlerNotFoundException(String message) {
    super(message);
  }
}
