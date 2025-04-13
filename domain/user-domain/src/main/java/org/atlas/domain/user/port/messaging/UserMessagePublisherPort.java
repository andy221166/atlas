package org.atlas.domain.user.port.messaging;

import org.atlas.framework.event.contract.user.UserRegisteredEvent;

public interface UserMessagePublisherPort {

  void publish(UserRegisteredEvent event);
}
