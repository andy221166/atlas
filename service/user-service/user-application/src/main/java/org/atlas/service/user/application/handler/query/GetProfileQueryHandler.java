package org.atlas.service.user.application.handler.query;

import lombok.RequiredArgsConstructor;
import org.atlas.commons.context.CurrentUserContext;
import org.atlas.commons.exception.AppError;
import org.atlas.commons.exception.BusinessException;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.cqrs.handler.QueryHandler;
import org.atlas.service.user.contract.query.GetUserQuery;
import org.atlas.service.user.contract.repository.UserRepository;
import org.atlas.service.user.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetProfileQueryHandler implements QueryHandler<GetUserQuery, ProfileDto> {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public ProfileDto handle(GetUserQuery query) throws Exception {
    Integer currentUserId = CurrentUserContext.getCurrentUserId();
    User user = userRepository.findById(currentUserId)
        .orElseThrow(() -> new BusinessException(AppError.USER_NOT_FOUND));
    return ModelMapperUtil.map(user, ProfileDto.class);
  }
}
