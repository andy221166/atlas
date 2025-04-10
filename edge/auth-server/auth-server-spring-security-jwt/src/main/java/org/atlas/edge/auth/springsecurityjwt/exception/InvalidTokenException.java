package org.atlas.edge.auth.springsecurityjwt.exception;

public class InvalidTokenException extends RuntimeException {

  public InvalidTokenException(String message) {
    super(message);
  }
}
