package org.atlas.domain.user.usecase.front;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.domain.user.entity.UserEntity;
import org.atlas.domain.user.port.messaging.UserMessagePublisherPort;
import org.atlas.domain.user.repository.FindUserCriteria;
import org.atlas.domain.user.repository.UserRepository;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.domain.user.usecase.front.FrontRegisterUseCaseHandler.RegisterInput;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.constant.Patterns;
import org.atlas.framework.domain.event.contract.user.UserRegisteredEvent;
import org.atlas.framework.domain.exception.DomainException;
import org.atlas.framework.error.AppError;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.auth.client.AuthClientPort;
import org.atlas.framework.auth.client.model.CreateUserRequest;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
@Slf4j
public class FrontRegisterUseCaseHandler implements UseCaseHandler<RegisterInput, Void> {

  private final UserRepository userRepository;
  private final AuthClientPort authClientPort;
  private final ApplicationConfigPort applicationConfigPort;
  private final UserMessagePublisherPort userMessagePublisherPort;

  @Override
  public Void handle(RegisterInput input) throws Exception {
    checkValidity(input);
    UserEntity userEntity = createUser(input);
    createAuthUser(userEntity);
    publishEvent(userEntity);
    return null;
  }

  private void checkValidity(RegisterInput input) {
    FindUserCriteria criteria = FindUserCriteria.builder()
        .username(input.getUsername())
        .email(input.getEmail())
        .phoneNumber(input.getPhoneNumber())
        .build();
    PagingRequest pagingRequest = PagingRequest.unpaged();
    PagingResult<UserEntity> pagingResult = userRepository.findByCriteria(criteria, pagingRequest);
    if (!pagingResult.checkEmpty()) {
      throw new DomainException(AppError.USER_ALREADY_EXISTS);
    }
  }

  private UserEntity createUser(RegisterInput input) {
    UserEntity userEntity = ObjectMapperUtil.getInstance()
        .map(input, UserEntity.class);
    userEntity.setRole(Role.USER);
    userRepository.insert(userEntity);
    return userEntity;
  }

  private void createAuthUser(UserEntity userEntity) {
    CreateUserRequest request = ObjectMapperUtil.getInstance()
        .map(userEntity, CreateUserRequest.class);
    authClientPort.createUser(request);
    log.info("Created auth user: userId={}, username={}",
        userEntity.getId(), userEntity.getUsername());
  }

  private void publishEvent(UserEntity userEntity) {
    UserRegisteredEvent event = new UserRegisteredEvent(applicationConfigPort.getApplicationName());
    ObjectMapperUtil.getInstance()
        .merge(userEntity, event);
    event.setUserId(userEntity.getId());
    userMessagePublisherPort.publish(event);
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
