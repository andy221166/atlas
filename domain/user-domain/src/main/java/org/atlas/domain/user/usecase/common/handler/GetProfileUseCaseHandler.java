package org.atlas.domain.user.usecase.common.handler;

import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.entity.UserEntity;
import org.atlas.domain.user.repository.UserRepository;
import org.atlas.framework.context.Contexts;
import org.atlas.framework.domain.exception.DomainException;
import org.atlas.framework.error.AppError;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class GetProfileUseCaseHandler implements UseCaseHandler<Void, UserEntity> {

  private final UserRepository userRepository;

  @Override
  public UserEntity handle(Void input) throws Exception {
    Integer userId = Contexts.getUserId();
    return userRepository.findById(userId)
        .orElseThrow(() -> new DomainException(AppError.USER_NOT_FOUND));
  }
}
