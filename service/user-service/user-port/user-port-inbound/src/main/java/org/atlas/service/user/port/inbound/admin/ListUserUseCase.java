package org.atlas.service.user.port.inbound.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.enums.Role;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.user.port.inbound.admin.ListUserUseCase.ListUserInput;
import org.atlas.service.user.port.inbound.admin.ListUserUseCase.ListUserOutput;
import org.atlas.service.user.port.inbound.admin.ListUserUseCase.ListUserOutput.User;

public interface ListUserUseCase extends UseCase<ListUserInput, ListUserOutput> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class ListUserInput {

    @Valid
    private PagingRequest pagingRequest;
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  class ListUserOutput extends PagingResult<User> {

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
