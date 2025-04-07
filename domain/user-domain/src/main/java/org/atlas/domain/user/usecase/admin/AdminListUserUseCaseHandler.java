package org.atlas.domain.user.usecase.admin;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.entity.UserEntity;
import org.atlas.domain.user.repository.UserRepository;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.domain.user.usecase.admin.AdminListUserUseCaseHandler.ListUserInput;
import org.atlas.domain.user.usecase.admin.AdminListUserUseCaseHandler.ListUserOutput;
import org.atlas.domain.user.usecase.admin.AdminListUserUseCaseHandler.ListUserOutput.User;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class AdminListUserUseCaseHandler implements UseCaseHandler<ListUserInput, ListUserOutput> {

  private final UserRepository userRepository;

  @Override
  public ListUserOutput handle(ListUserInput input) throws Exception {
    PagingResult<UserEntity> userEntityPage = userRepository.findAll(input.getPagingRequest());
    PagingResult<User> userPage = ObjectMapperUtil.getInstance()
        .mapPage(userEntityPage, User.class);
    return new ListUserOutput(userPage.getResults(), userPage.getPagination());
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ListUserInput {

    @Valid
    private PagingRequest pagingRequest;
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ListUserOutput extends PagingResult<User> {

    public ListUserOutput(List<User> results, Pagination pagination) {
      super(results, pagination);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {

      private Integer id;
      private String username;
      private String firstName;
      private String lastName;
      private String email;
      private String phoneNumber;
      private Role role;
    }
  }
}