package org.atlas.platform.notification.email.core.service;

import org.atlas.platform.notification.email.core.model.SendEmailRequest;

public interface EmailService {

  void send(SendEmailRequest request);
}
