package org.atlas.service.user.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.enums.Role;
import org.atlas.platform.commons.event.DomainEvent;
import org.atlas.platform.commons.event.EventType;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRegisteredEvent extends DomainEvent {

  private Integer id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private Role role;

  public UserRegisteredEvent(String eventSource) {
    super(eventSource);
  }

  @Override
  @NonNull
  public EventType getEventType() {
    return EventType.USER_REGISTERED;
  }
}
