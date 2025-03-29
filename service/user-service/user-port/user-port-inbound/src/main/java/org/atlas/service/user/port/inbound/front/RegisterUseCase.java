package org.atlas.service.user.port.inbound.front;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.constant.Patterns;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.user.port.inbound.front.RegisterUseCase.RegisterInput;

public interface RegisterUseCase extends UseCase<RegisterInput, Void> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class RegisterInput {

    @NotBlank
    private String username;

    @NotBlank
    @Pattern(regexp = Patterns.PASSWORD)
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phoneNumber;
  }
}
