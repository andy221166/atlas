package org.atlas.port.inbound.user.internal;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.enums.Role;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.platform.usecase.port.input.InternalInput;
import org.atlas.port.inbound.user.internal.ListUserUseCase.ListUserInput;
import org.atlas.port.inbound.user.internal.ListUserUseCase.ListUserOutput;

public interface ListUserUseCase extends UseCase<ListUserInput, ListUserOutput> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode(callSuper = false)
  class ListUserInput extends InternalInput {

    @NotEmpty
    private List<Integer> ids;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class ListUserOutput {

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
