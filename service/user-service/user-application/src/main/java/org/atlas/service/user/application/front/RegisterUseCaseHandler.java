package org.atlas.service.user.application.front;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.commons.enums.AppError;
import org.atlas.platform.commons.enums.Role;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.config.ApplicationConfigService;
import org.atlas.platform.event.contract.user.UserRegisteredEvent;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.domain.entity.UserEntity;
import org.atlas.service.user.port.inbound.front.RegisterUseCase;
import org.atlas.service.user.port.outbound.auth.AuthPort;
import org.atlas.service.user.port.outbound.event.UserEventPublisherPort;
import org.atlas.service.user.port.outbound.repository.UserRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterUseCaseHandler implements RegisterUseCase {

  private final UserRepositoryPort userRepositoryPort;
  private final AuthPort authPort;
  private final UserEventPublisherPort userEventPublisherPort;
  private final ApplicationConfigService applicationConfigService;

  @Override
  @Transactional
  public Void handle(RegisterInput input) throws Exception {
    checkValidity(input);
    UserEntity userEntity = createUser(input);
    syncAuth(userEntity);
    publishEvent(userEntity);
    return null;
  }

  private void checkValidity(RegisterInput input) {
    if (userRepositoryPort.findByUsername(input.getUsername()).isPresent()) {
      throw new BusinessException(AppError.USERNAME_ALREADY_EXISTS);
    }
    if (userRepositoryPort.findByEmail(input.getEmail()).isPresent()) {
      throw new BusinessException(AppError.EMAIL_ALREADY_EXISTS);
    }
    if (userRepositoryPort.findByPhoneNumber(input.getPhoneNumber()).isPresent()) {
      throw new BusinessException(AppError.PHONE_NUMBER_ALREADY_EXISTS);
    }
  }

  private UserEntity createUser(RegisterInput input) {
    UserEntity userEntity = ObjectMapperUtil.getInstance().map(input, UserEntity.class);
    userEntity.setRole(Role.USER);
    userRepositoryPort.insert(userEntity);
    return userEntity;
  }

  private void syncAuth(UserEntity userEntity) {
    authPort.register(userEntity);
  }

  private void publishEvent(UserEntity userEntity) {
    UserRegisteredEvent event = new UserRegisteredEvent(
        applicationConfigService.getApplicationName());
    ObjectMapperUtil.getInstance().merge(userEntity, event);
    userEventPublisherPort.publish(event);
  }
}
