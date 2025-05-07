package org.atlas.domain.user.usecase.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.entity.UserEntity;
import org.atlas.domain.user.repository.FindUserCriteria;
import org.atlas.domain.user.repository.UserRepository;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.domain.user.usecase.admin.AdminListUserUseCaseHandler.ListUserInput;
import org.atlas.domain.user.usecase.admin.AdminListUserUseCaseHandler.UserOutput;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class AdminListUserUseCaseHandler implements
    UseCaseHandler<ListUserInput, PagingResult<UserOutput>> {

  private final UserRepository userRepository;

  @Override
  public PagingResult<UserOutput> handle(ListUserInput input) throws Exception {
    FindUserCriteria criteria = ObjectMapperUtil.getInstance()
        .map(input, FindUserCriteria.class);
    PagingResult<UserEntity> userEntityPage =
        userRepository.findByCriteria(criteria, input.getPagingRequest());
    return ObjectMapperUtil.getInstance()
        .mapPage(userEntityPage, UserOutput.class);
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ListUserInput {

    private Integer id;
    private String username;
    private String email;
    private String phoneNumber;

    @Valid
    private PagingRequest pagingRequest;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UserOutput {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;
  }
}