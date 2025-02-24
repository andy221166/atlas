package org.atlas.service.user.port.inbound.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.enums.Role;
import org.atlas.platform.commons.paging.PageResult;
import org.atlas.platform.commons.paging.PageRequest;
import org.atlas.platform.usecase.port.UseCase;

public interface ListUserUseCase
    extends UseCase<ListUserUseCase.Input, ListUserUseCase.Output> {

  @Data
  @EqualsAndHashCode(callSuper = false)
  class Input {

    @NotEmpty
    private String keyword;

    @Valid
    private PageRequest pageRequest;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode(callSuper = false)
  class Output {

    private PageResult<User> users;

    @Data
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
