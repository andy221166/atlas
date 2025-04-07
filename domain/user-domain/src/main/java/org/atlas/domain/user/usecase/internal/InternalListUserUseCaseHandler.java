package org.atlas.domain.user.usecase.internal;

import jakarta.validation.constraints.NotEmpty;
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
import org.atlas.domain.user.usecase.internal.InternalListUserUseCaseHandler.ListUserInput;
import org.atlas.domain.user.usecase.internal.InternalListUserUseCaseHandler.ListUserOutput;
import org.atlas.domain.user.usecase.internal.InternalListUserUseCaseHandler.ListUserOutput.User;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.usecase.handler.UseCaseHandler;
import org.atlas.framework.usecase.input.InternalInput;

@RequiredArgsConstructor
public class InternalListUserUseCaseHandler implements
    UseCaseHandler<ListUserInput, ListUserOutput> {

  private final UserRepository userRepository;

  @Override
  public ListUserOutput handle(ListUserInput input) throws Exception {
    List<UserEntity> userEntities = userRepository.findByIdIn(input.getIds());
    List<User> users = ObjectMapperUtil.getInstance()
        .mapList(userEntities, User.class);
    return new ListUserOutput(users);
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode(callSuper = false)
  public static class ListUserInput extends InternalInput {

    @NotEmpty
    private List<Integer> ids;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ListUserOutput {

    private List<User> users;

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
