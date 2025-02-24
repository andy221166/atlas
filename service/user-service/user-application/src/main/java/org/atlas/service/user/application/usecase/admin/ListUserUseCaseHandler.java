package org.atlas.service.user.application.usecase.admin;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.paging.PageResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.domain.entity.UserEntity;
import org.atlas.service.user.port.inbound.admin.ListUserUseCase;
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
    FindUserCriteria criteria = ObjectMapperUtil.getInstance()
        .map(input, FindUserCriteria.class);
    PageResult<UserEntity> userEntityPageResult =
        userRepository.findByCriteria(criteria, input.getPageRequest());
    PageResult<Output.User> userPageResult = ObjectMapperUtil.getInstance()
        .mapPage(userEntityPageResult, Output.User.class);
    return new Output(userPageResult);
  }
}
