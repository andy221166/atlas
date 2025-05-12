package org.atlas.domain.user.usecase.admin.handler;

import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.entity.UserEntity;
import org.atlas.domain.user.repository.FindUserCriteria;
import org.atlas.domain.user.repository.UserRepository;
import org.atlas.domain.user.usecase.admin.model.AdminListUserInput;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class AdminListUserUseCaseHandler implements
    UseCaseHandler<AdminListUserInput, PagingResult<UserEntity>> {

  private final UserRepository userRepository;

  @Override
  public PagingResult<UserEntity> handle(AdminListUserInput input) throws Exception {
    FindUserCriteria criteria = ObjectMapperUtil.getInstance()
        .map(input, FindUserCriteria.class);
    return userRepository.findByCriteria(criteria, input.getPagingRequest());
  }
}
