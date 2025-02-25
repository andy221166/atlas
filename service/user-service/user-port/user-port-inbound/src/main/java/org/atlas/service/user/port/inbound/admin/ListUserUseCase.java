package org.atlas.service.user.port.inbound.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.enums.Role;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.usecase.port.UseCase;

public interface ListUserUseCase
    extends UseCase<ListUserUseCase.Input, ListUserUseCase.Output> {

  @Data
  class Input {

    @NotEmpty
    private String keyword;

    @Valid
    private PagingRequest pagingRequest;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

    private PagingResult<User> users;

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
