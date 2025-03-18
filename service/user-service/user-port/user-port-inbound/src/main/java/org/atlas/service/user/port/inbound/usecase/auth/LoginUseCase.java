package org.atlas.service.user.port.inbound.usecase.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;

public interface LoginUseCase
    extends UseCase<LoginUseCase.Input, LoginUseCase.Output> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class Input {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

    private String accessToken;
    private String refreshToken;
  }
}
