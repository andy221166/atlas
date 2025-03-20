package org.atlas.service.notification.port.outbound.email;

public interface EmailPort {

  void send(SendEmailRequest request);
}
