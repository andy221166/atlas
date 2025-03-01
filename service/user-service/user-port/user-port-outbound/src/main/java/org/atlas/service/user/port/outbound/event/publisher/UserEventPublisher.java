package org.atlas.service.user.port.outbound.event.publisher;

import org.atlas.platform.event.contract.user.UserRegisteredEvent;

public interface UserEventPublisher {

  void publish(UserRegisteredEvent event);
}
