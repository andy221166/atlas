package org.atlas.service.user.application.usecase.auth;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.context.UserContext;
import org.atlas.platform.commons.exception.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.platform.usecase.port.input.EmptyInput;
import org.atlas.service.user.domain.entity.UserEntity;
import org.atlas.service.user.port.inbound.storefront.GetProfileUseCase;
import org.atlas.service.user.port.outbound.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetProfileUseCaseHandler implements GetProfileUseCase {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public Output handle(EmptyInput input) throws Exception {
    Integer userId = UserContext.getUserId();
    UserEntity userEntity = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(AppError.USER_NOT_FOUND));
    return ObjectMapperUtil.getInstance().map(userEntity, Output.class);
  }
}
