package org.atlas.domain.user.usecase.front;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.domain.user.entity.UserEntity;
import org.atlas.domain.user.repository.UserRepository;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.domain.user.usecase.front.FrontRegisterUseCaseHandler.RegisterInput;
import org.atlas.framework.auth.AuthPort;
import org.atlas.framework.auth.model.RegisterRequest;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.constant.Patterns;
import org.atlas.framework.cryptography.PasswordUtil;
import org.atlas.framework.error.AppError;
import org.atlas.framework.event.contract.user.UserRegisteredEvent;
import org.atlas.framework.event.publisher.UserEventPublisherPort;
import org.atlas.framework.exception.BusinessException;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.usecase.UseCaseHandler;

@RequiredArgsConstructor
@Slf4j
public class FrontRegisterUseCaseHandler implements UseCaseHandler<RegisterInput, Void> {

  private final UserRepository userRepository;
  private final ApplicationConfigPort applicationConfigPort;
  private final UserEventPublisherPort userEventPublisherPort;
  private final AuthPort authPort;

  @Override
  public Void handle(RegisterInput input) throws Exception {
    checkValidity(input);
    UserEntity userEntity = createUser(input);
    syncAuth(userEntity);
    publishEvent(userEntity);
    return null;
  }

  private void checkValidity(RegisterInput input) {
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

  private UserEntity createUser(RegisterInput input) {
    UserEntity userEntity = ObjectMapperUtil.getInstance()
        .map(input, UserEntity.class);
    userEntity.setPlainPassword(input.getPassword());
    userEntity.setHashedPassword(PasswordUtil.hashPassword(input.getPassword()));
    userEntity.setRole(Role.USER);
    userRepository.insert(userEntity);
    return userEntity;
  }

  private void syncAuth(UserEntity userEntity) {
    RegisterRequest request = new RegisterRequest();
    request.setUsername(userEntity.getUsername());
    request.setPlainPassword(userEntity.getPlainPassword());
    request.setHashedPassword(userEntity.getHashedPassword());
    request.setClaims(Map.of(
        "id", userEntity.getId(),
        "email", userEntity.getEmail(),
        "role", userEntity.getRole()
    ));
    authPort.register(request);
  }

  private void publishEvent(UserEntity userEntity) {
    UserRegisteredEvent event = new UserRegisteredEvent(
        applicationConfigPort.getApplicationName());
    ObjectMapperUtil.getInstance()
        .merge(userEntity, event);
    userEventPublisherPort.publish(event);
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RegisterInput {

    @NotBlank
    private String username;

    @NotBlank
    @Pattern(regexp = Patterns.PASSWORD)
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phoneNumber;
  }
}
