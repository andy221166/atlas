package org.atlas.service.user.application.usecase.internal;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.domain.entity.UserEntity;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase;
import org.atlas.service.user.port.outbound.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("internalListUserUseCaseHandler")
@RequiredArgsConstructor
public class ListUserUseCaseHandler implements ListUserUseCase {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public Output handle(Input input) throws Exception {
    List<UserEntity> userEntities = userRepository.findByIdIn(input.getIds());
    List<Output.User> users = userEntities.stream()
        .map(user -> ObjectMapperUtil.getInstance().map(user, Output.User.class))
        .toList();
    return new Output(users);
  }
}
