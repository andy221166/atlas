package org.atlas.framework.event.publisher;

import org.atlas.framework.event.contract.user.UserRegisteredEvent;

public interface UserEventPublisherPort {

  void publish(UserRegisteredEvent event);
}
