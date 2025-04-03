package org.atlas.port.inbound.user.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.port.inbound.user.auth.LogoutUseCase.LogoutInput;

public interface LogoutUseCase extends UseCase<LogoutInput, Void> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class LogoutInput {

    @NotBlank
    private String accessToken;
  }
}
