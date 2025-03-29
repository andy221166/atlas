package org.atlas.service.user.application.internal;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.domain.entity.UserEntity;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase.ListUserOutput.User;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase;
import org.atlas.service.user.port.outbound.repository.UserRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("internalListUserUseCaseHandler")
@RequiredArgsConstructor
public class ListUserUseCaseHandler implements ListUserUseCase {

  private final UserRepositoryPort userRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public ListUserOutput handle(ListUserInput input) throws Exception {
    List<UserEntity> userEntities = userRepositoryPort.findByIdIn(input.getIds());
    List<User> users = ObjectMapperUtil.getInstance().mapList(userEntities, User.class);
    return new ListUserOutput(users);
  }
}
