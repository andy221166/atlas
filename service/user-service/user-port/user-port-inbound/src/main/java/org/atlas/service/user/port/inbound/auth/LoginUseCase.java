package org.atlas.service.user.port.inbound.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.usecase.port.UseCase;

public interface LoginUseCase
    extends UseCase<LoginUseCase.Input, LoginUseCase.Output> {

  @Data
  @EqualsAndHashCode(callSuper = false)
  class Input {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  class Output {

    private String accessToken;
    private String refreshToken;
  }
}
