package org.atlas.domain.user.usecase.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.entity.UserEntity;
import org.atlas.domain.user.repository.UserRepository;
import org.atlas.domain.user.usecase.common.GetProfileUseCaseHandler.GetProfileOutput;
import org.atlas.framework.security.session.SessionContext;
import org.atlas.framework.error.AppError;
import org.atlas.framework.exception.BusinessException;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class GetProfileUseCaseHandler implements UseCaseHandler<Void, GetProfileOutput> {

  private final UserRepository userRepository;

  @Override
  public GetProfileOutput handle(Void input) throws Exception {
    Integer userId = SessionContext.getUserId();
    UserEntity userEntity = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(AppError.USER_NOT_FOUND));
    return ObjectMapperUtil.getInstance()
        .map(userEntity, GetProfileOutput.class);
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetProfileOutput {

    private String firstName;
    private String lastName;
  }
}
