package org.atlas.service.user.port.inbound.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.user.port.inbound.auth.LoginUseCase.LoginInput;
import org.atlas.service.user.port.inbound.auth.LoginUseCase.LoginOutput;

public interface LoginUseCase extends UseCase<LoginInput, LoginOutput> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class LoginInput {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class LoginOutput {

    private String accessToken;
    private String refreshToken;
  }
}
