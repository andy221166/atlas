package org.atlas.service.user.port.inbound.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.atlas.platform.usecase.port.UseCase;

public interface LoginUseCase
    extends UseCase<LoginUseCase.Input, LoginUseCase.Output> {

  @Data
  class Input {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
  }

  @Data
  class Output {

    private String accessToken;
    private String refreshToken;
  }
}
