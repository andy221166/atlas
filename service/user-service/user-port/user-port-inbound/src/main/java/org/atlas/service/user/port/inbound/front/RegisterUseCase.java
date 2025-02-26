package org.atlas.service.user.port.inbound.front;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.atlas.platform.commons.constant.Patterns;
import org.atlas.platform.usecase.port.UseCase;

public interface RegisterUseCase
    extends UseCase<RegisterUseCase.Input, Void> {

  @Data
  class Input {

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
