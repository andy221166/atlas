package org.atlas.service.user.port.outbound.event.publisher;

import org.atlas.service.user.domain.event.UserRegisteredEvent;

public interface UserEventPublisher {

  void publish(UserRegisteredEvent event);
}
