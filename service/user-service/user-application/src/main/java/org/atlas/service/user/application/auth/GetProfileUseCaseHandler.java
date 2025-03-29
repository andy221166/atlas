package org.atlas.service.user.application.auth;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.context.UserContext;
import org.atlas.platform.commons.enums.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.domain.entity.UserEntity;
import org.atlas.service.user.port.inbound.front.GetProfileUseCase;
import org.atlas.service.user.port.outbound.repository.UserRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetProfileUseCaseHandler implements GetProfileUseCase {

  private final UserRepositoryPort userRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public GetProfileOutput handle(Void input) throws Exception {
    Integer userId = UserContext.getUserId();
    UserEntity userEntity = userRepositoryPort.findById(userId)
        .orElseThrow(() -> new BusinessException(AppError.USER_NOT_FOUND));
    return ObjectMapperUtil.getInstance().map(userEntity, GetProfileOutput.class);
  }
}
