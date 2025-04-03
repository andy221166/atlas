package org.atlas.port.outbound.notification.email;

public interface EmailPort {

  void notify(EmailNotification notification);
}
