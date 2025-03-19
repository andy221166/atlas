package org.atlas.service.notification.adapter.email.ses.exception;

public class SendEmailException extends RuntimeException {

  public SendEmailException(Throwable cause) {
    super(cause);
  }
}
