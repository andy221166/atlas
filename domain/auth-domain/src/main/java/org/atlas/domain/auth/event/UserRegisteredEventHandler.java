package org.atlas.domain.auth.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.domain.auth.entity.AuthUserEntity;
import org.atlas.domain.auth.repository.AuthUserRepository;
import org.atlas.framework.event.contract.user.UserRegisteredEvent;
import org.atlas.framework.event.handler.EventHandler;
import org.atlas.framework.objectmapper.ObjectMapperUtil;

@RequiredArgsConstructor
@Slf4j
public class UserRegisteredEventHandler implements EventHandler<UserRegisteredEvent> {

  private final AuthUserRepository authUserRepository;

  @Override
  public void handle(UserRegisteredEvent event) {
    AuthUserEntity authUserEntity = ObjectMapperUtil.getInstance()
        .map(event, AuthUserEntity.class);
    authUserRepository.insert(authUserEntity);
  }
}
