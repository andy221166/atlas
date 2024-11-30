package org.atlas.service.user.application.handler.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.cqrs.handler.QueryHandler;
import org.atlas.service.user.contract.model.UserDto;
import org.atlas.service.user.contract.query.ListUserQuery;
import org.atlas.service.user.contract.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListUserQueryHandler implements QueryHandler<ListUserQuery, List<UserDto>> {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> handle(ListUserQuery query) throws Exception {
    return userRepository.findByIdIn(query.getIds())
        .stream()
        .map(user -> ModelMapperUtil.map(user, UserDto.class))
        .toList();
  }
}
