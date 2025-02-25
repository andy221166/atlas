package org.atlas.service.user.application.usecase.admin;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.domain.entity.UserEntity;
import org.atlas.service.user.port.inbound.admin.ListUserUseCase;
import org.atlas.service.user.port.inbound.admin.ListUserUseCase.Output.User;
import org.atlas.service.user.port.outbound.repository.FindUserCriteria;
import org.atlas.service.user.port.outbound.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("adminListUserUseCaseHandler")
@RequiredArgsConstructor
public class ListUserUseCaseHandler implements ListUserUseCase {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public Output handle(Input input) throws Exception {
    FindUserCriteria criteria = ObjectMapperUtil.getInstance().map(input, FindUserCriteria.class);
    PagingResult<UserEntity> userEntityPage = userRepository.findByCriteria(criteria, input.getPagingRequest());
    PagingResult<User> userPage = ObjectMapperUtil.getInstance().mapPage(userEntityPage, Output.User.class);
    return new Output(userPage);
  }
}
