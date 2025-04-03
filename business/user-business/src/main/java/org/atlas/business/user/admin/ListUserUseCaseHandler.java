package org.atlas.business.user.admin;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.domain.user.entity.UserEntity;
import org.atlas.port.inbound.user.admin.ListUserUseCase;
import org.atlas.port.inbound.user.admin.ListUserUseCase.ListUserOutput.User;
import org.atlas.service.user.port.outbound.repository.UserRepositoryPort;

@RequiredArgsConstructor
public class ListUserUseCaseHandler implements ListUserUseCase {

  private final UserRepositoryPort userRepositoryPort;

  @Override
  public ListUserOutput handle(ListUserInput input) throws Exception {
    PagingResult<UserEntity> userEntityPage = userRepositoryPort.findAll(input.getPagingRequest());
    PagingResult<User> userPage = ObjectMapperUtil.getInstance()
        .mapPage(userEntityPage, User.class);
    return new ListUserOutput(userPage.getResults(), userPage.getPagination());
  }
}
