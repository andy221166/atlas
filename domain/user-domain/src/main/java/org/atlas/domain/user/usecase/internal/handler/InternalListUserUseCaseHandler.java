package org.atlas.domain.user.usecase.internal.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.entity.UserEntity;
import org.atlas.domain.user.repository.UserRepository;
import org.atlas.domain.user.usecase.internal.model.InternalListUserInput;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class InternalListUserUseCaseHandler implements
    UseCaseHandler<InternalListUserInput, List<UserEntity>> {

  private final UserRepository userRepository;

  @Override
  public List<UserEntity> handle(InternalListUserInput input) throws Exception {
    return userRepository.findByIdIn(input.getIds());
  }
}
