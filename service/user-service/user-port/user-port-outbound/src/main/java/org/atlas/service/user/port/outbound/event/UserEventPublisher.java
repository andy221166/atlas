package org.atlas.service.user.port.outbound.event;

import org.atlas.platform.event.contract.user.UserRegisteredEvent;

public interface UserEventPublisher {

  void publish(UserRegisteredEvent event);
}
