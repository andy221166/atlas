package org.atlas.service.user.application.handler.query;

import lombok.RequiredArgsConstructor;
import org.atlas.commons.context.CurrentUserContext;
import org.atlas.commons.exception.AppError;
import org.atlas.commons.exception.BusinessException;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.cqrs.handler.QueryHandler;
import org.atlas.service.user.contract.model.ProfileDto;
import org.atlas.service.user.contract.query.GetProfileQuery;
import org.atlas.service.user.contract.repository.CustomerRepository;
import org.atlas.service.user.contract.repository.UserRepository;
import org.atlas.service.user.domain.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetProfileQueryHandler implements QueryHandler<GetProfileQuery, ProfileDto> {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Override
    public ProfileDto handle(GetProfileQuery query) throws Exception {
        Integer currentUserId = CurrentUserContext.getCurrentUserId();
        User user = userRepository.findById(currentUserId)
            .orElseThrow(() -> new BusinessException(AppError.USER_NOT_FOUND));
        return ModelMapperUtil.map(user, ProfileDto.class);
    }
}
