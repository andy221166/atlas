package org.atlas.service.user.application.usecase.storefront;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.commons.enums.Role;
import org.atlas.platform.commons.exception.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.usecase.port.output.EmptyOutput;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.domain.entity.UserEntity;
import org.atlas.service.user.domain.event.UserRegisteredEvent;
import org.atlas.service.user.port.inbound.storefront.RegisterUseCase;
import org.atlas.service.user.port.outbound.auth.AuthService;
import org.atlas.service.user.port.outbound.event.publisher.UserEventPublisher;
import org.atlas.service.user.port.outbound.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterUseCaseHandler implements RegisterUseCase {

  private final UserRepository userRepository;
  private final AuthService authService;
  private final UserEventPublisher userEventPublisher;

  @Value("${spring.application.name}")
  private String applicationName;

  @Override
  @Transactional
  public EmptyOutput handle(Input input) throws Exception {
    checkValidity(input);
    UserEntity userEntity = createUser(input);
    syncAuth(userEntity);
    publishEvent(userEntity);
    return null;
  }

  private void checkValidity(Input input) {
    if (userRepository.findByUsername(input.getUsername()).isPresent()) {
      throw new BusinessException(AppError.USERNAME_ALREADY_EXISTS);
    }
    if (userRepository.findByEmail(input.getEmail()).isPresent()) {
      throw new BusinessException(AppError.EMAIL_ALREADY_EXISTS);
    }
    if (userRepository.findByPhoneNumber(input.getPhoneNumber()).isPresent()) {
      throw new BusinessException(AppError.PHONE_NUMBER_ALREADY_EXISTS);
    }
  }

  private UserEntity createUser(Input input) {
    UserEntity userEntity = ObjectMapperUtil.getInstance().map(input, UserEntity.class);
    userEntity.setRole(Role.USER);
    userRepository.insert(userEntity);
    return userEntity;
  }

  private void syncAuth(UserEntity userEntity) {
    authService.register(userEntity);
  }

  private void publishEvent(UserEntity userEntity) {
    UserRegisteredEvent event = new UserRegisteredEvent(applicationName);
    ObjectMapperUtil.getInstance().merge(userEntity, event);
    userEventPublisher.publish(event);
  }
}
