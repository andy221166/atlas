package org.atlas.service.notification.port.outbound.email;

public class SendEmailException extends RuntimeException {

  public SendEmailException(Throwable cause) {
    super(cause);
  }
}
